package com.team1.mvp_test.domain.enterprise.dto

data class EnterpriseSignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val ceoName: String,
    val phoneNumber: String,
)