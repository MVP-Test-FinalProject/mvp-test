package com.team1.mvp_test.domain.member.controller

import com.team1.mvp_test.domain.member.dto.MemberResponse
import com.team1.mvp_test.domain.member.dto.MemberUpdateRequest
import com.team1.mvp_test.domain.member.dto.MemberUpdateResponse
import com.team1.mvp_test.domain.member.dto.SignUpInfoRequest
import com.team1.mvp_test.domain.member.service.MemberService
import com.team1.mvp_test.infra.security.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("api/v1/members")
@RestController
class MemberController(
    val memberService: MemberService
) {
    @PutMapping("/info")
    @PreAuthorize("hasRole('MEMBER')")
    fun updateSignUpInfo(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @Valid @RequestBody request: SignUpInfoRequest
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateSignUpInfo(userPrincipal.id, request))
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('MEMBER')")
    fun updateMember(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @Valid @RequestBody request: MemberUpdateRequest
    ): ResponseEntity<MemberUpdateResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(memberService.updateMember(userPrincipal.id, request))
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