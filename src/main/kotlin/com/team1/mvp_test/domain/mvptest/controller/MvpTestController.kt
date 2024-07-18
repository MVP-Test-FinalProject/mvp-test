package com.team1.mvp_test.domain.mvptest.controller

import com.team1.mvp_test.domain.mvptest.dto.mvptest.*
import com.team1.mvp_test.domain.mvptest.service.MvpTestService
import com.team1.mvp_test.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mvp_tests")
class MvpTestController(
    private val mvpTestService: MvpTestService
) {

    @PostMapping("")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun createMvpTest(
        @RequestBody request: CreateMvpTestRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<MvpTestResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(mvpTestService.createMvpTest(userPrincipal.id, request))
    }

    @PutMapping("/{testId}")
    @PreAuthorize("hasRole('ENTERPRISE')")
    fun updateMvpTest(
        @RequestBody request: UpdateMvpTestRequest,
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

    @GetMapping("")
    fun getMvpTestList(
    ): ResponseEntity<MvpTestListResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mvpTestService.getMvpTestList())
    }

    @PostMapping("/category")
    //@PreAuthorize("hasRole('ADMIN')")
    fun createCategory(
        @RequestBody request: CreateCategoryRequest
    ): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mvpTestService.createCategory(request))
    }

    @PreAuthorize("hasRole('ENTERPRISE')")
    @PostMapping("/{testId}/approve")
    fun approveMemberToMvpTest(
        @RequestParam memberId: Long,
        @PathVariable("testId") testId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<TestingMemberCountResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mvpTestService.approveMemberToTest())
    }
}