package com.team1.mvp_test.domain.member.controller

import com.team1.mvp_test.domain.member.dto.MemberResponse
import com.team1.mvp_test.domain.member.dto.MemberUpdateRequest
import com.team1.mvp_test.domain.member.dto.MemberUpdateResponse
import com.team1.mvp_test.domain.member.dto.SignUpRequest
import com.team1.mvp_test.domain.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/members")
@RestController
class MemberController(
    val memberService: MemberService
) {
    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(memberService.signUp(request))
    }

    @PutMapping("/{memberId}/profile")
    fun updateMember(
        @PathVariable memberId: Long,
        @RequestBody request: MemberUpdateRequest
    ): ResponseEntity<MemberUpdateResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateMember(request, memberId))
    }

    @GetMapping("/{memberId}")
    fun getMemberById(
        @PathVariable memberId: Long,
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.getMemberById(memberId))
    }
}