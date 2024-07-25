package com.team1.mvp_test.domain.member.service

import com.team1.mvp_test.common.error.MemberErrorMessage
import com.team1.mvp_test.common.exception.ModelNotFoundException
import com.team1.mvp_test.domain.member.dto.MemberResponse
import com.team1.mvp_test.domain.member.dto.MemberUpdateRequest
import com.team1.mvp_test.domain.member.dto.MemberUpdateResponse
import com.team1.mvp_test.domain.member.dto.SignUpInfoRequest
import com.team1.mvp_test.domain.member.model.Member
import com.team1.mvp_test.domain.member.repository.MemberRepository
import com.team1.mvp_test.domain.oauth.client.OAuthLoginUserInfo
import com.team1.mvp_test.infra.redis.RedisService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val redisService: RedisService,
) {
    @Transactional
    fun updateSignUpInfo(memberId: Long, request: SignUpInfoRequest): MemberResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId)

        val verifiedPhoneNumber = redisService.getVerifiedPhoneNumber(request.phoneNumber)
        check(verifiedPhoneNumber != null && verifiedPhoneNumber == request.phoneNumber)
        { MemberErrorMessage.PHONENUMBER_VERIFY_FAIL }
        check(!memberRepository.existsByPhoneNumber(request.phoneNumber))
        { MemberErrorMessage.PHONENUMBER_ALREADY_EXISTS }

        member.updateSignUpInfo(
            name = request.name,
            age = request.age,
            sex = request.sex,
            info = request.info,
            phoneNumber = request.phoneNumber
        )
        return MemberResponse.from(member)
    }

    @Transactional
    fun updateMember(memberId: Long, request: MemberUpdateRequest): MemberUpdateResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId)
        member.updateMember(
            name = request.name,
            info = request.info
        )
        return MemberUpdateResponse.from(member)
    }

    fun getMemberById(memberId: Long): MemberResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("Member", memberId)
        return MemberResponse.from(member)
    }

    @Transactional
    fun registerIfAbsent(userInfo: OAuthLoginUserInfo): Member {
        return memberRepository.findByProviderIdAndProviderName(userInfo.id, userInfo.provider) ?: run {
            Member(
                email = userInfo.email,
                providerId = userInfo.id,
                providerName = userInfo.provider
            ).let { memberRepository.save(it) }
        }
    }
}