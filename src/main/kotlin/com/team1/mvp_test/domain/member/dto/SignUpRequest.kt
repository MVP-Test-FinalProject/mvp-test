package com.team1.mvp_test.domain.member.dto

data class SignUpRequest(
    val email: String,
    val name: String,
    val age: Long,
    val sex: String,
    val info: String,
)
