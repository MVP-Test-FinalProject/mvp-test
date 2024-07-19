package com.team1.mvp_test.domain.report.service

import com.team1.mvp_test.common.error.ReportErrorMessage
import com.team1.mvp_test.common.exception.ModelNotFoundException
import com.team1.mvp_test.common.exception.NoPermissionException
import com.team1.mvp_test.domain.member.model.MemberTest
import com.team1.mvp_test.domain.member.repository.MemberTestRepository
import com.team1.mvp_test.domain.mvptest.model.MvpTest

import com.team1.mvp_test.domain.report.dto.ApproveReportRequest
import com.team1.mvp_test.domain.report.dto.ApproveReportResponse
import com.team1.mvp_test.domain.report.dto.ReportResponse
import com.team1.mvp_test.domain.report.dto.UpdateReportRequest
import com.team1.mvp_test.domain.report.model.Report
import com.team1.mvp_test.domain.report.model.ReportMedia
import com.team1.mvp_test.domain.report.repository.ReportMediaRepository
import com.team1.mvp_test.domain.report.repository.ReportRepository
import com.team1.mvp_test.domain.step.repository.StepRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReportService(
    private val reportRepository: ReportRepository,
    private val reportMediaRepository: ReportMediaRepository,
    private val stepRepository: StepRepository,
    private val memberTestRepository: MemberTestRepository,
) {

    @Transactional
    fun createReport(stepId: Long, request: UpdateReportRequest, memberId: Long): ReportResponse {
        val step = stepRepository.findByIdOrNull(stepId) ?: throw ModelNotFoundException("step", stepId)
        val test = step.mvpTest
        checkDateCondition(test)
        val memberTest = checkMemberTest(test, memberId)

        val media = request.mediaUrl.map { reportMediaRepository.save(ReportMedia(mediaUrl = it)) }.toMutableList()
        validateMediaCount(media)
        val report = Report(
            title = request.title,
            body = request.body,
            feedback = request.feedback,
            memberTest = memberTest,
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
        reportId: Long,
        request: UpdateReportRequest,
        memberId: Long
    ): ReportResponse {
        val report = reportRepository.findByIdOrNull(reportId) ?: throw ModelNotFoundException("report", reportId)
        validateAlreadyConfirmed(report)
        val test = report.step.mvpTest
        checkDateCondition(test)
        checkMemberTest(test, memberId)
        checkAuthor(report, memberId)

        report.title = request.title
        report.body = request.body
        report.feedback = request.feedback

        report.reportMedia.clear()
        val newMedia = request.mediaUrl.map { reportMediaRepository.save(ReportMedia(mediaUrl = it)) }.toMutableList()
        report.reportMedia = newMedia
        validateMediaCount(newMedia)

        return ReportResponse.from(report)
    }

    @Transactional
    fun deleteReport(reportId: Long, memberId: Long) {
        val report = reportRepository.findByIdOrNull(reportId) ?: throw ModelNotFoundException("report", reportId)

        checkAuthor(report, memberId)
        validateAlreadyConfirmed(report)

        reportRepository.delete(report)
        report.reportMedia.clear()
    }

    @Transactional
    fun approveReport(
        reportId: Long,
        request: ApproveReportRequest,
        enterpriseId: Long
    ): ApproveReportResponse {
        val report = reportRepository.findByIdOrNull(reportId) ?: throw ModelNotFoundException("report", reportId)
        validateAlreadyConfirmed(report)
        val test = report.step.mvpTest
        checkEnterprise(test, enterpriseId)

        report.isConfirmed = request.isConfirmed
        report.reason = request.reason

        reportRepository.save(report)

        return ApproveReportResponse.from(report)
    }

    private fun checkDateCondition(test: MvpTest) {
        val currentDate = LocalDateTime.now()
        if (currentDate.isAfter(test.testEndDate) || currentDate.isBefore(test.testStartDate)) {
            throw IllegalArgumentException(ReportErrorMessage.NOT_TEST_DURATION.message)
        }
    }

    private fun checkAuthor(report: Report, memberId: Long) {
        check(report.memberTest.member.id == memberId) { ReportErrorMessage.NOT_AUTHORIZED.message }
    }

    private fun checkEnterprise(test: MvpTest, enterpriseId: Long) {
        check(test.enterpriseId == enterpriseId) { ReportErrorMessage.NOT_YOUR_TEST.message }
    }

    private fun checkMemberTest(test: MvpTest, memberId: Long): MemberTest {
        val memberTest = memberTestRepository.findByMemberIdAndTestId(memberId, test.id) ?: throw NoPermissionException(
            ReportErrorMessage.NO_PERMISSION.message
        )
        return memberTest
    }

    private fun validateMediaCount(reportMedia: List<ReportMedia>) {
        check(reportMedia.size <= 10) { throw IllegalArgumentException(ReportErrorMessage.MEDIA_COUNT_OVER.message) }
    }

    private fun validateAlreadyConfirmed(report: Report) {
        if (report.isConfirmed) {
            throw IllegalArgumentException(ReportErrorMessage.ALREADY_CONFIRMED_REPORT.message)
        }
    }

}