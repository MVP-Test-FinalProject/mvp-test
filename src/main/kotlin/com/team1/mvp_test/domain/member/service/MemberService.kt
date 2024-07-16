package com.team1.mvp_test.domain.member.service

import com.team1.mvp_test.domain.common.error.MemberErrorMessage
import com.team1.mvp_test.domain.common.exception.ModelNotFoundException
import com.team1.mvp_test.domain.member.dto.MemberResponse
import com.team1.mvp_test.domain.member.dto.MemberUpdateRequest
import com.team1.mvp_test.domain.member.dto.MemberUpdateResponse
import com.team1.mvp_test.domain.member.dto.SignUpRequest
import com.team1.mvp_test.domain.member.model.Member
import com.team1.mvp_test.domain.member.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun signUp(request: SignUpRequest): MemberResponse {
        check(!memberRepository.existsByEmail(request.email)) { MemberErrorMessage.EMAIL_ALREADY_IN_USE.message }
        return Member(
            email = request.email,
            name = request.name,
            age = request.age,
            sex = request.sex,
            info = request.info,
        ).let { memberRepository.save(it) }
            .let { MemberResponse.from(it) }
    }

    @Transactional
    fun updateMember(request: MemberUpdateRequest, memberId: Long): MemberUpdateResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId)
        member.updateMember(request)
        return MemberUpdateResponse.from(member)
    }

    fun getMemberById(memberId: Long): MemberResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId)
        return MemberResponse.from(member)
    }
}