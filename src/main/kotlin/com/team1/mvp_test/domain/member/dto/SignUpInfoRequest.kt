package com.team1.mvp_test.domain.member.dto

import com.team1.mvp_test.domain.member.model.Sex
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignUpInfoRequest(
    @field:NotBlank
    val name: String,

    @field:Min(value = 15)
    @field:Max(value = 100)
    val age: Int,

    val sex: Sex,

    @field:Size(max = 500)
    val info: String?,
)
