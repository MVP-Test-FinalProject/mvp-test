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
@RequestMapping("/mvp_tests/{testId}/steps")
class StepController(
    private val stepService: StepService
) {

    @GetMapping
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun getStepList(
        @PathVariable testId: Long,
        @AuthenticationPrincipal  userPrincipal: UserPrincipal
    ):ResponseEntity<List<StepListResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(stepService.getStepList(userPrincipal.id,testId))
    }

    @PostMapping
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun createStep(
        @PathVariable testId: Long,
        @RequestBody request: CreateStepRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<StepResponse> {
       return ResponseEntity
           .status(HttpStatus.CREATED)
           .body(stepService.createStep(userPrincipal.id,testId,request))
    }

    @GetMapping("/{stepId}")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun getStepById(
        @PathVariable testId: Long,
        @PathVariable stepId:Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ):ResponseEntity<StepResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(stepService.getStepById(userPrincipal.id,testId,stepId))
    }

    @PutMapping("/{stepId}")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun updateStepById(
        @PathVariable testId: Long,
        @PathVariable stepId: Long,
        @RequestBody request: UpdateStepRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ):ResponseEntity<StepResponse> {
       return ResponseEntity
            .status(HttpStatus.OK)
            .body(stepService.updateStepById(userPrincipal.id,testId,stepId,request))
    }

    @DeleteMapping("/{stepId}")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun deleteStepById(
        @PathVariable testId: Long,
        @PathVariable stepId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ):ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(stepService.deleteStepById(userPrincipal.id,testId,stepId))
    }

}