package com.team1.mvp_test.domain.enterprise.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateEnterpriseRequest(

    @field:Size(min = 1)
    val name: String,

    @field:Size(min = 1)
    val ceoName: String,

    @field:Size(min = 10, max = 11)
    @field:Pattern(regexp = "^01([0-1])([0-9]{3,4})([0-9]){4}$")
    val phoneNumber: String,
)