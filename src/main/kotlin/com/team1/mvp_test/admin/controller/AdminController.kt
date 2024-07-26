package com.team1.mvp_test.admin.controller

import com.team1.mvp_test.admin.dto.AdminLoginRequest
import com.team1.mvp_test.admin.dto.AdminLoginResponse
import com.team1.mvp_test.admin.service.AdminAuthService
import com.team1.mvp_test.admin.service.AdminService
import com.team1.mvp_test.domain.enterprise.dto.EnterpriseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService,
    private val adminAuthService: AdminAuthService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/enterprises")
    fun getAllEnterprises(): ResponseEntity<List<EnterpriseResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(adminService.getAllEnterprises())
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/enterprises/{enterpriseId}/approve")
    fun approveEnterprise(
        @PathVariable enterpriseId: Long,
    ): ResponseEntity<EnterpriseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(adminService.approveEnterprise(enterpriseId))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: AdminLoginRequest
    ): ResponseEntity<AdminLoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(adminAuthService.login(request))
    }
}