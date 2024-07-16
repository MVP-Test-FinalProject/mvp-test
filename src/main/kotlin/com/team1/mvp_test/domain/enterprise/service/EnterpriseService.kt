package com.team1.mvp_test.domain.enterprise.service

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
        check(enterpriseRepository.existsByName(request.name)) { "이미 존재하는 기업입니다." }
        check(enterpriseRepository.existsByEmail(request.email)) { "이미 존재하는 기업입니다." }
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
        val enterprise = enterpriseRepository.findByEmail(request.email) ?: throw RuntimeException("존재하지 않는 기업입니다.")
        if (enterprise.password != request.password) throw RuntimeException("비밀번호가 맞지 않습니다.")
        return EnterpriseLoginResponse(
            // TODO: Security 적용시 수정해야 할 부분
            accessToken = ""
        )
    }

    fun getProfile(enterpriseId: Long): EnterpriseResponse {
        val enterprise = enterpriseRepository.findByIdOrNull(enterpriseId) ?: throw RuntimeException("존재하지 않는 기업입니다.")
        return EnterpriseResponse.from(enterprise)
    }

    @Transactional
    fun updateProfile(enterpriseId: Long, request: UpdateEnterpriseRequest): EnterpriseResponse {
        val enterprise = enterpriseRepository.findByIdOrNull(enterpriseId) ?: throw RuntimeException("존재하지 않는 기업입니다.")
        return enterprise.apply {
            this.name = request.name
            this.ceoName = request.ceoName
            this.phoneNumber = request.phoneNumber
        }.let { EnterpriseResponse.from(it) }
    }
}