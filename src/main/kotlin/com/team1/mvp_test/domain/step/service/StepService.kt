package com.team1.mvp_test.domain.step.service

import com.team1.mvp_test.common.error.MvpTestErrorMessage
import com.team1.mvp_test.common.exception.ModelNotFoundException
import com.team1.mvp_test.common.exception.NoPermissionException
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import com.team1.mvp_test.domain.step.dto.CreateStepRequest
import com.team1.mvp_test.domain.step.dto.StepListResponse
import com.team1.mvp_test.domain.step.dto.StepResponse
import com.team1.mvp_test.domain.step.dto.UpdateStepRequest
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
    private val s3Service: S3Service
) {
    fun getStepList(enterpriseId: Long, testId: Long): List<StepListResponse> {
        val mvpTest = mvpTestRepository.findByIdOrNull(testId)
            ?: throw ModelNotFoundException("MvpTest", testId)
        if (mvpTest.enterpriseId != enterpriseId) throw NoPermissionException(MvpTestErrorMessage.NOT_AUTHORIZED.message)
        return stepRepository.findAllByMvpTestIdOrderByStepOrder(testId).map { StepListResponse.from(it) }
    }

    @Transactional
    fun createStep(enterpriseId: Long, testId: Long, request: CreateStepRequest,guidelineFile:MultipartFile?): StepResponse {
        val mvpTest = mvpTestRepository.findByIdOrNull(testId)
            ?: throw ModelNotFoundException("MvpTest", testId)
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
    fun updateStep(enterpriseId: Long, stepId: Long, request: UpdateStepRequest, guidelineFile: MultipartFile?): StepResponse {
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
        stepRepository.delete(step)
    }
}