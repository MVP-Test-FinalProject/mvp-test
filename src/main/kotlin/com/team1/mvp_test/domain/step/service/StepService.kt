package com.team1.mvp_test.domain.step.service

import com.team1.mvp_test.common.error.MvpTestErrorMessage
import com.team1.mvp_test.common.error.StepErrorMessage
import com.team1.mvp_test.common.exception.ModelNotFoundException
import com.team1.mvp_test.common.exception.NoPermissionException
import com.team1.mvp_test.domain.member.repository.MemberTestRepository
import com.team1.mvp_test.domain.mvptest.model.MvpTestState
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import com.team1.mvp_test.domain.report.model.ReportState
import com.team1.mvp_test.domain.report.repository.ReportRepository
import com.team1.mvp_test.domain.step.dto.*
import com.team1.mvp_test.domain.step.model.Step
import com.team1.mvp_test.domain.step.repository.StepRepository
import com.team1.mvp_test.infra.s3.s3service.S3Service
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class StepService(
    private val stepRepository: StepRepository,
    private val mvpTestRepository: MvpTestRepository,
    private val s3Service: S3Service,
    private val memberTestRepository: MemberTestRepository,
    private val reportRepository: ReportRepository
) {
    fun getStepList(enterpriseId: Long, testId: Long): List<StepListResponse> {
        val mvpTest = mvpTestRepository.findByIdOrNull(testId)
            ?: throw ModelNotFoundException("MvpTest", testId)
        if (mvpTest.enterpriseId != enterpriseId) throw NoPermissionException(MvpTestErrorMessage.NOT_AUTHORIZED.message)
        return stepRepository.findAllByMvpTestIdOrderByStepOrder(testId).map { StepListResponse.from(it) }
    }

    @Transactional
    fun createStep(
        enterpriseId: Long,
        testId: Long,
        request: CreateStepRequest,
        guidelineFile: MultipartFile?
    ): StepResponse {
        val mvpTest = mvpTestRepository.findByIdOrNull(testId)
            ?: throw ModelNotFoundException("MvpTest", testId)
        if (mvpTest.state != MvpTestState.APPROVED) throw NoPermissionException(StepErrorMessage.NOT_APPROVED_TEST.message)
        if (mvpTest.enterpriseId != enterpriseId) throw NoPermissionException(MvpTestErrorMessage.NOT_AUTHORIZED.message)
        val maxOrder = stepRepository.findMaxOrderByTestId(testId)
        val file = guidelineFile?.let { s3Service.uploadStepFile(it) }
        return Step(
            title = request.title,
            requirement = request.requirement,
            guidelineUrl = file,
            reward = request.reward,
            stepOrder = maxOrder + 1,
            mvpTest = mvpTest
        ).let { stepRepository.save(it) }
            .let { StepResponse.from(it) }
    }

    fun getStepById(stepId: Long): StepResponse {
        val step = stepRepository.findByIdOrNull(stepId)
            ?: throw ModelNotFoundException("Step", stepId)
        return StepResponse.from(step)
    }

    @Transactional
    fun updateStep(
        enterpriseId: Long,
        stepId: Long,
        request: UpdateStepRequest,
        guidelineFile: MultipartFile?
    ): StepResponse {
        val step = stepRepository.findByIdOrNull(stepId)
            ?: throw ModelNotFoundException("Step", stepId)
        if (step.mvpTest.enterpriseId != enterpriseId) throw NoPermissionException(MvpTestErrorMessage.NOT_AUTHORIZED.message)
        step.guidelineUrl?.let { s3Service.deleteFile(it) }
        val file = guidelineFile?.let { s3Service.uploadStepFile(it) }
        step.updateStep(
            title = request.title,
            requirement = request.requirement,
            guidelineUrl = file,
        )
        return StepResponse.from(step)
    }

    @Transactional
    fun deleteStep(enterpriseId: Long, stepId: Long) {
        val step = stepRepository.findByIdOrNull(stepId)
            ?: throw ModelNotFoundException("Step", stepId)
        if (step.mvpTest.enterpriseId != enterpriseId) throw NoPermissionException(MvpTestErrorMessage.NOT_AUTHORIZED.message)
        step.guidelineUrl?.let { s3Service.deleteFile(it) }
        stepRepository.delete(step)
    }

    fun getStepOverview(enterpriseId: Long, stepId: Long): StepOverviewResponse {
        val step = stepRepository.findByIdOrNull(stepId)
            ?: throw ModelNotFoundException("Step", stepId)
        if (enterpriseId != step.mvpTest.enterpriseId)
            throw NoPermissionException(MvpTestErrorMessage.NOT_AUTHORIZED.message)
        //테스트 참여자, 해당 step의 리포트 한꺼번에 불러오기
        val members = memberTestRepository.findAllAndMemberByTestId(step.mvpTest.id!!)
        val reports = reportRepository.findAllByStepId(stepId)
        //각각의 참여자, 리포트를 memberId로 묶어주고 반환 형태 만들기
        val reportMap = reports.associateBy({ it.memberTest.member.id }, { it.state })
        val reportStatusResponse = members.map { memberTest ->
            val reportState = reportMap[memberTest.member.id] ?: ReportState.MISSING
            ReportStatusResponse(
                memberId = memberTest.member.id!!,
                memberEmail = memberTest.member.email,
                completionState = reportState
            )
        }
        //참여율 백분율로 계산
        val approvedCount = reportStatusResponse.count { it.completionState == ReportState.APPROVED }
        val totalCount = reportStatusResponse.size
        val completionRate = if (totalCount > 0) {
            ((approvedCount.toDouble() / totalCount) * 100).toInt()
        } else {
            0
        }
        return StepOverviewResponse(
            completionRate = completionRate,
            reportStatusList = reportStatusResponse
        )
    }
}