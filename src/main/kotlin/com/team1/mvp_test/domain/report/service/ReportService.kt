package com.team1.mvp_test.domain.report.service

import com.team1.mvp_test.common.exception.ModelNotFoundException
import com.team1.mvp_test.domain.member.repository.MemberTestRepository
import com.team1.mvp_test.domain.mvptest.dto.report.*
import com.team1.mvp_test.domain.report.model.Report
import com.team1.mvp_test.domain.report.model.ReportMedia
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import com.team1.mvp_test.domain.report.repository.ReportMediaRepository
import com.team1.mvp_test.domain.report.repository.ReportRepository
import com.team1.mvp_test.domain.report.dto.ApproveReportRequest
import com.team1.mvp_test.domain.report.dto.ApproveReportResponse
import com.team1.mvp_test.domain.report.dto.ReportResponse
import com.team1.mvp_test.domain.report.dto.UpdateReportRequest
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportRepository: ReportRepository,
    private val reportMediaRepository: ReportMediaRepository,
    private val mvpTestRepository: MvpTestRepository,
    private val stepRepository: StepRepository,
    private val memberTestRepository: MemberTestRepository,
) {

    @Transactional
    fun createReport(testId: Long, stepId: Long, request: UpdateReportRequest, memberId: Long): ReportResponse {
        val test = mvpTestRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException("mvp test", testId)
        val step = stepRepository.findByIdOrNull(stepId) ?: throw ModelNotFoundException("step", stepId)
        val memberTest = memberTestRepository.findByMemberIdAndTestId(memberId, testId) ?: throw RuntimeException("you are not tester for this test")

        val media = request.mediaUrl.map { reportMediaRepository.save(ReportMedia(mediaUrl = it))}.toMutableList()
        val report = Report(
            title = request.title,
            body = request.body,
            feedback = request.feedback,
            memberTest = memberTest,
            test = test,
            step = step,
            isConfirmed = false,
            reason = null,
            reportMedia = media
        )
        return report.let { reportRepository.save(it) }
            .let { ReportResponse.from(it) }
    }

    @Transactional
    fun updateReport(
        testId: Long,
        stepId: Long,
        reportId: Long,
        request: UpdateReportRequest,
        memberId: Long
    ): ReportResponse {
        val test = mvpTestRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException("mvp test", testId)
        val step = stepRepository.findByIdOrNull(stepId) ?: throw ModelNotFoundException("step", stepId)
        val memberTest = memberTestRepository.findByMemberIdAndTestId(memberId, testId) ?: throw RuntimeException("you are not tester for this test")

        val report = reportRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException("report", testId)
        report.title = request.title
        report.body = request.body
        report.feedback = request.feedback

        report.reportMedia.clear()
        val newMedia = request.mediaUrl.map{ reportMediaRepository.save(ReportMedia(mediaUrl = it)) }

        return ReportResponse.from(report)
    }

    fun deleteReport(testId: Long, stepId: Long, reportId: Long, memberId: Long) {
        val test = mvpTestRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException("mvp test", testId)
        val step = stepRepository.findByIdOrNull(stepId) ?: throw ModelNotFoundException("step", stepId)
        val memberTest = memberTestRepository.findByMemberIdAndTestId(memberId, testId) ?: throw RuntimeException("you are not tester for this test")


        val report = reportRepository.findByIdOrNull(reportId) ?: throw ModelNotFoundException("report", reportId)
        reportRepository.delete(report)
    }

    fun approveReport(
        testId: Long,
        stepId: Long,
        reportId: Long,
        request: ApproveReportRequest,
        enterpriseId: Long
    ): ApproveReportResponse? {
        val report = reportRepository.findByIdOrNull(reportId) ?: throw ModelNotFoundException("report", reportId)
        report.isConfirmed = request.isConfirmed
        report.reason = request.reason
        reportRepository.save(report)
        return ApproveReportResponse.from(report)
    }
}