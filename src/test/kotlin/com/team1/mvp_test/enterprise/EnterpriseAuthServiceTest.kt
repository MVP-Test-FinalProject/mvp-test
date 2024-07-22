package com.team1.mvp_test.enterprise

import com.team1.mvp_test.common.exception.PasswordIncorrectException
import com.team1.mvp_test.domain.enterprise.dto.EnterpriseSignUpRequest
import com.team1.mvp_test.domain.enterprise.dto.LoginRequest
import com.team1.mvp_test.domain.enterprise.model.Enterprise
import com.team1.mvp_test.domain.enterprise.repository.EnterpriseRepository
import com.team1.mvp_test.domain.enterprise.service.EnterpriseAuthService
import com.team1.mvp_test.infra.security.jwt.JwtHelper
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class EnterpriseAuthServiceTest {
    @Mock
    private lateinit var enterpriseRepository: EnterpriseRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var jwtHelper: JwtHelper

    @InjectMocks
    private lateinit var enterpriseAuthService: EnterpriseAuthService

    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun `signUp Email Validation Fail`() {
        val invalidEmailRequest = EnterpriseSignUpRequest(
            email = "invalid_email",
            name = "이름",
            ceoName = "ceo 이름",
            password = "Password0123",
            phoneNumber = "01012345678"
        )
        val violations = validator.validate(invalidEmailRequest)
        assert(violations.isEmpty())
    }

    @Test
    fun `signUp password Validation Fail`() {
        val invalidPasswordRequest = EnterpriseSignUpRequest(
            email = "email@email.com",
            name = "이름",
            ceoName = "ceo 이름",
            password = "InvalidPassword",
            phoneNumber = "01012345678"
        )
        val violations = validator.validate(invalidPasswordRequest)
        assert(violations.isEmpty())
    }

    @Test
    fun `Login Password Fail`() {
        val invalidPasswordLoginRequest = LoginRequest(
            email = "email@email.com",
            password = "WrongPassword"
        )
        val enterprise = Enterprise(
            id = 1L,
            email = "email@email.com",
            name = "이름",
            ceoName = "ceo 이름",
            password = BCryptPasswordEncoder().encode("Password0123"),
            phoneNumber = "01012345678"
        )
        `when`(enterpriseRepository.findByEmail(invalidPasswordLoginRequest.email)).thenReturn(enterprise)
        `when`(passwordEncoder.matches(invalidPasswordLoginRequest.password, enterprise.password)).thenReturn(false)

        assertThrows(PasswordIncorrectException::class.java) {
            enterpriseAuthService.login(invalidPasswordLoginRequest)
        }
    }
}