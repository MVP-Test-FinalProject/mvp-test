package com.team1.mvp_test.domain.step.dto

data class UpdateStepRequest(
    val title: String,
    val requirement: String,
    val guidelineUrl: String?
)
