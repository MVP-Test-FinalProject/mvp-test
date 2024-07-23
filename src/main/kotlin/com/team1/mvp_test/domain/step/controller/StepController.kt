package com.team1.mvp_test.domain.step.controller

import com.team1.mvp_test.domain.step.dto.CreateStepRequest
import com.team1.mvp_test.domain.step.dto.StepListResponse
import com.team1.mvp_test.domain.step.dto.StepResponse
import com.team1.mvp_test.domain.step.dto.UpdateStepRequest
import com.team1.mvp_test.domain.step.service.StepService
import com.team1.mvp_test.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1")
class StepController(
    private val stepService: StepService
) {

    @GetMapping("/mvp-tests/{testId}/steps")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun getStepList(
        @PathVariable testId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<List<StepListResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(stepService.getStepList(userPrincipal.id, testId))
    }

    @PostMapping("/mvp-tests/{testId}/steps")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun createStep(
        @PathVariable testId: Long,
        @RequestBody request: CreateStepRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<StepResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(stepService.createStep(userPrincipal.id, testId, request))
    }

    @GetMapping("steps/{stepId}")
    fun getStepById(
        @PathVariable stepId: Long,
    ): ResponseEntity<StepResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(stepService.getStepById(stepId))
    }

    @PutMapping("steps/{stepId}")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun updateStepById(
        @PathVariable stepId: Long,
        @RequestBody request: UpdateStepRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<StepResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(stepService.updateStep(userPrincipal.id, stepId, request))
    }

    @DeleteMapping("steps/{stepId}")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun deleteStepById(
        @PathVariable stepId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(stepService.deleteStep(userPrincipal.id, stepId))
    }

}