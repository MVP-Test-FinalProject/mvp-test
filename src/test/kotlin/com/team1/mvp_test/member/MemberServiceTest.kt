package com.team1.mvp_test.member

import com.team1.mvp_test.domain.member.repository.MemberRepository
import com.team1.mvp_test.domain.member.service.MemberService
import com.team1.mvp_test.domain.oauth.client.OAuthClientService
import com.team1.mvp_test.domain.oauth.client.OAuthLoginUserInfo
import com.team1.mvp_test.domain.oauth.provider.OAuthProvider
import com.team1.mvp_test.infra.security.jwt.JwtHelper
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class MemberServiceTest {
    @Mock
    private lateinit var memberRepository: MemberRepository

    @Mock
    private lateinit var oAuthClientService: OAuthClientService

    @Mock
    private lateinit var jwtHelper: JwtHelper

    @InjectMocks
    private lateinit var memberService: MemberService

    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun `login should return OAuthLoginUserInfo`() {
        val provider = OAuthProvider.NAVER
        val code = "authorizeCode"
        val userInfo = OAuthLoginUserInfo(
            provider = provider,
            id = "123",
            email = "email@email.com"
        )

        `when`(oAuthClientService.login(provider, code)).thenReturn(userInfo)

        val result = oAuthClientService.login(provider, code)

        assertEquals(userInfo, result)
    }
}