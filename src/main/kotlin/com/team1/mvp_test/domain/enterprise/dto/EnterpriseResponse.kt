package com.team1.mvp_test.domain.enterprise.dto

data class EnterpriseResponse(
    val id: Long,
    val email: String,
    val name: String,
    val ceoName: String,
    val phoneNumber: String,
)