package com.team1.mvp_test.domain.report.dto

import com.team1.mvp_test.domain.report.model.Report
import com.team1.mvp_test.domain.report.model.ReportMedia

data class ReportResponse (

    val id: Long,

    val stepId: Long,

    val testId: Long,

    val body: String,

    val reportMedia: List<ReportMedia>,

    val feedback: String,

    val isConfirmed: Boolean,

    val reason: String?,

    ) {
    companion object {
        fun from(report: Report): ReportResponse {
            return ReportResponse(
                id = report.id!!,
                stepId = report.step.id!!,
                testId = report.test.id!!,
                body = report.body,
                reportMedia = report.reportMedia,
                feedback = report.feedback,
                isConfirmed = report.isConfirmed,
                reason = report.reason
            )
        }
    }
}