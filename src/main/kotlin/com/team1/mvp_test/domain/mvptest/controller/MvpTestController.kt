package com.team1.mvp_test.domain.mvptest.controller

import com.team1.mvp_test.domain.mvptest.dto.mvptest.CreateMpvTestRequest
import com.team1.mvp_test.domain.mvptest.dto.mvptest.MvpTestResponse
import com.team1.mvp_test.domain.mvptest.service.MvpTestService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("")
class MvpTestController(
    private val mvpTestService: MvpTestService
) {

    @PostMapping("/mvp_tests")
    fun createMvpTest(
        @RequestBody request: CreateMpvTestRequest
    ): ResponseEntity<MvpTestResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(mvpTestService.createMvpTest(request))
    }


    @PutMapping("/{testId}")
    fun updateMvpTest(
        @RequestBody request: UpdateMvpTestRequest,
        @PathVariable("testId") testId: Long
    ): ResponseEntity<MvpTestResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(mvpTestService.updateMvpTest())
    }

    @DeleteMapping("/{testId}")
    fun deleteMvpTest(
        @PathVariable ("testId") testId: Long,
    ): ResponseEntity<Unit> {
        mvpTestService.deleteMvpTest(testId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

    @GetMapping("/{testId}")
    fun getMvpTest(
        @PathVariable ("testId") testId: Long,
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

}