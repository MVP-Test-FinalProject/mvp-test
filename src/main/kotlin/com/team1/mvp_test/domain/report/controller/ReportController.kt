package com.team1.mvp_test.domain.report.controller

import com.team1.mvp_test.domain.report.dto.ReportRequest
import com.team1.mvp_test.domain.report.dto.ReportResponse
import com.team1.mvp_test.domain.report.service.ReportService
import com.team1.mvp_test.infra.security.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/v1")
class ReportController(
    private val reportService: ReportService
) {
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/steps/{step-id}/reports")
    fun createReport(
        @PathVariable("step-id") stepId: Long,
        @Valid @RequestPart("request") request: ReportRequest,
        @RequestPart("mediaFiles") mediaFiles: MutableList<MultipartFile>,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<ReportResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reportService.createReport(userPrincipal.id, stepId, request, mediaFiles))
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/reports/{report-id}")
    fun updateReport(
        @PathVariable("report-id") reportId: Long,
        @Valid @RequestPart("request") request: ReportRequest,
        @RequestPart("mediaFiles") mediaFiles: MutableList<MultipartFile>,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<ReportResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reportService.updateReport(userPrincipal.id, reportId, request, mediaFiles))
    }

    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/reports/{report-id}")
    fun deleteReport(
        @PathVariable("report-id") reportId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(reportService.deleteReport(userPrincipal.id, reportId))
    }

    @PreAuthorize("hasRole('ENTERPRISE')")
    @PutMapping("/reports/{report-id}/approve")
    fun approveReport(
        @PathVariable("report-id") reportId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<ReportResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reportService.approveReport(userPrincipal.id, reportId))
    }

}