package com.team1.mvp_test.domain.step.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class CreateStepRequest(
    @field:Size(min = 5, max = 20, message = "")
    val title: String,

    @field:Size(min = 10, max = 200, message = "")
    val requirement: String,

    val guidelineUrl: String?,

    @field:Min(value = 1000, message = "")
    val reward: Int
)
