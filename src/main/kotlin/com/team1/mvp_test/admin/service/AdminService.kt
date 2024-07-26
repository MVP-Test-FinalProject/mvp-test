package com.team1.mvp_test.admin.service

import com.team1.mvp_test.domain.enterprise.dto.EnterpriseResponse
import com.team1.mvp_test.domain.enterprise.service.EnterpriseService
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val enterpriseService: EnterpriseService,

    ) {

    fun getAllEnterprises(): List<EnterpriseResponse> {
        return enterpriseService.getAllEnterprises()
    }

    fun approveEnterprise(enterpriseId: Long): EnterpriseResponse {
        return enterpriseService.approveEnterprise(enterpriseId)
    }


}