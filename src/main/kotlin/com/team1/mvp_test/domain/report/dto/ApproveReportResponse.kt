package com.team1.mvp_test.domain.report.dto

import com.team1.mvp_test.domain.report.model.Report

data class ApproveReportResponse (

    val isConfirmed: Boolean,

    val reason: String?,

) {
    companion object {
        fun from(report: Report): ApproveReportResponse {
            return ApproveReportResponse(
                isConfirmed = report.isConfirmed,
                reason = report.reason
            )
        }
    }
}