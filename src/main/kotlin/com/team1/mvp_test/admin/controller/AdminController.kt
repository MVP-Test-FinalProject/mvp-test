package com.team1.mvp_test.admin.controller

import com.team1.mvp_test.admin.dto.AdminLoginRequest
import com.team1.mvp_test.admin.dto.AdminLoginResponse
import com.team1.mvp_test.admin.service.AdminAuthService
import com.team1.mvp_test.batch.service.BatchService
import com.team1.mvp_test.domain.enterprise.dto.EnterpriseResponse
import com.team1.mvp_test.domain.enterprise.service.EnterpriseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/admin")
class AdminController(
    private val enterpriseService: EnterpriseService,
    private val adminAuthService: AdminAuthService,
    private val batchService: BatchService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/enterprises")
    fun getAllEnterprises(): ResponseEntity<List<EnterpriseResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(enterpriseService.getAllEnterprises())
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/enterprises/{enterpriseId}/approve")
    fun approveEnterprise(
        @PathVariable enterpriseId: Long,
    ): ResponseEntity<EnterpriseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(enterpriseService.approveEnterprise(enterpriseId))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: AdminLoginRequest
    ): ResponseEntity<AdminLoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(adminAuthService.login(request))
    }

    @PostMapping("/rewards/settle")
    fun settleReward(): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(batchService.provideRewardAllTests(LocalDateTime.now()))
    }
}