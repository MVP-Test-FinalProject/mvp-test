package com.team1.mvp_test.domain.mvptest.controller

import com.team1.mvp_test.domain.mvptest.dto.CreateMvpTestRequest
import com.team1.mvp_test.domain.mvptest.dto.MvpTestResponse
import com.team1.mvp_test.domain.mvptest.dto.UpdateMvpTestRequest
import com.team1.mvp_test.domain.mvptest.service.MvpTestService
import com.team1.mvp_test.infra.security.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/mvp-tests")
class MvpTestController(
    private val mvpTestService: MvpTestService
) {

    @PostMapping
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun createMvpTest(
        @Valid @RequestBody request: CreateMvpTestRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MvpTestResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(mvpTestService.createMvpTest(userPrincipal.id, request))
    }

    @PutMapping("/{testId}")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun updateMvpTest(
        @Valid @RequestBody request: UpdateMvpTestRequest,
        @PathVariable("testId") testId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MvpTestResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mvpTestService.updateMvpTest(userPrincipal.id, testId, request))
    }

    @DeleteMapping("/{testId}")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun deleteMvpTest(
        @PathVariable("testId") testId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(mvpTestService.deleteMvpTest(userPrincipal.id, testId))
    }

    @GetMapping("/{testId}")
    fun getMvpTest(
        @PathVariable("testId") testId: Long,
    ): ResponseEntity<MvpTestResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mvpTestService.getMvpTest(testId))
    }

    @GetMapping
    fun getMvpTestList(
    ): ResponseEntity<List<MvpTestResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mvpTestService.getMvpTestList())
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{testId}/apply")
    fun applyToMvpTest(
        @PathVariable("testId") testId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mvpTestService.applyToMvpTest(userPrincipal.id, testId))
    }
}