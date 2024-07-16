package com.team1.mvp_test.domain.common.exception

data class PasswordIncorrectException(
    override val message: String? = "Incorrect Password",
) : RuntimeException()