package com.team1.mvp_test.domain.enterprise.controller

import com.team1.mvp_test.domain.enterprise.dto.*
import com.team1.mvp_test.domain.enterprise.service.EnterpriseAuthService
import com.team1.mvp_test.domain.enterprise.service.EnterpriseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enterprise")
class EnterpriseController(
    private val enterpriseService: EnterpriseService,
    private val enterpriseAuthService: EnterpriseAuthService
) {

    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: EnterpriseSignUpRequest
    ): ResponseEntity<EnterpriseResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(enterpriseAuthService.signUp(request))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<EnterpriseLoginResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(enterpriseAuthService.login(request))
    }
    
    @GetMapping("/{enterpriseId}/profile")
    fun getProfile(
        @PathVariable enterpriseId: Long,
    ): ResponseEntity<EnterpriseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(enterpriseService.getProfile(enterpriseId))
    }

    @PutMapping("/{enterpriseId}/profile")
    fun updateProfile(
        @PathVariable enterpriseId: Long,
        @RequestBody request: UpdateEnterpriseRequest
    ): ResponseEntity<EnterpriseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(enterpriseService.updateProfile(enterpriseId, request))
    }
}