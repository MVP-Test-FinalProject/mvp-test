package com.team1.mvp_test.domain.report.controller

import com.team1.mvp_test.domain.report.dto.ApproveReportRequest
import com.team1.mvp_test.domain.report.dto.ApproveReportResponse
import com.team1.mvp_test.domain.report.dto.ReportResponse
import com.team1.mvp_test.domain.report.dto.UpdateReportRequest
import com.team1.mvp_test.domain.report.service.ReportService
import com.team1.mvp_test.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("mvp-test/{test-id}/steps/{step-id}/reports")
class ReportController(
    private val reportService: ReportService
) {

    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping
    fun createReport(
        @PathVariable("test-id") testId: Long,
        @PathVariable("step-id") stepId: Long,
        @RequestBody request: UpdateReportRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<ReportResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reportService.createReport(stepId, request, userPrincipal.id))
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/{report-id}")
    fun updateReport(
        @PathVariable("test-id") testId: Long,
        @PathVariable("step-id") stepId: Long,
        @PathVariable("report-id") reportId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: UpdateReportRequest
    ): ResponseEntity<ReportResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reportService.updateReport(reportId, request, userPrincipal.id))
    }

    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{report-id}")
    fun deleteReport(
        @PathVariable("test-id") testId: Long,
        @PathVariable("step-id") stepId: Long,
        @PathVariable("report-id") reportId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<Unit> {
        reportService.deleteReport(reportId, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

    @PreAuthorize("hasRole('ENTERPRISE')")
    @PutMapping("/{report-id}/approve")
    fun approveReport(
        @PathVariable("test-id") testId: Long,
        @PathVariable("step-id") stepId: Long,
        @PathVariable("report-id") reportId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: ApproveReportRequest
    ): ResponseEntity<ApproveReportResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reportService.approveReport(reportId, request, userPrincipal.id))
    }

}