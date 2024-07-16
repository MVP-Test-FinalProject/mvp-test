package com.team1.mvp_test.domain.enterprise.service

import com.team1.mvp_test.domain.common.error.EnterpriseErrorMessage
import com.team1.mvp_test.domain.common.exception.ModelNotFoundException
import com.team1.mvp_test.domain.common.exception.PasswordIncorrectException
import com.team1.mvp_test.domain.enterprise.dto.*
import com.team1.mvp_test.domain.enterprise.model.Enterprise
import com.team1.mvp_test.domain.enterprise.repository.EnterpriseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EnterpriseService(
    private val enterpriseRepository: EnterpriseRepository
) {
    fun signUp(request: EnterpriseSignUpRequest): EnterpriseResponse {
        check(!enterpriseRepository.existsByName(request.name)) { EnterpriseErrorMessage.ALREADY_EXISTS.message }
        check(!enterpriseRepository.existsByEmail(request.email)) { EnterpriseErrorMessage.ALREADY_EXISTS.message }
        return Enterprise(
            email = request.email,
            name = request.name,
            ceoName = request.ceoName,
            password = request.password,
            phoneNumber = request.phoneNumber
        ).let { enterpriseRepository.save(it) }
            .let { EnterpriseResponse.from(it) }
    }

    fun login(request: LoginRequest): EnterpriseLoginResponse {
        val enterprise = enterpriseRepository.findByEmail(request.email)
            ?: throw ModelNotFoundException("Enterprise", request.email)
        if (enterprise.password != request.password) throw PasswordIncorrectException()
        return EnterpriseLoginResponse(
            // TODO: Security 적용시 수정해야 할 부분
            accessToken = ""
        )
    }

    fun getProfile(enterpriseId: Long): EnterpriseResponse {
        val enterprise = enterpriseRepository.findByIdOrNull(enterpriseId) ?: throw ModelNotFoundException(
            "Enterprise",
            enterpriseId
        )
        return EnterpriseResponse.from(enterprise)
    }

    @Transactional
    fun updateProfile(enterpriseId: Long, request: UpdateEnterpriseRequest): EnterpriseResponse {
        val enterprise = enterpriseRepository.findByIdOrNull(enterpriseId) ?: throw ModelNotFoundException(
            "Enterprise",
            enterpriseId
        )
        return enterprise.apply {
            this.name = request.name
            this.ceoName = request.ceoName
            this.phoneNumber = request.phoneNumber
        }.let { EnterpriseResponse.from(it) }
    }
}