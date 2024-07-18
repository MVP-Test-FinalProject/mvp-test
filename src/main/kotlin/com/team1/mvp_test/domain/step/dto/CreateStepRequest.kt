package com.team1.mvp_test.domain.step.dto

data class CreateStepRequest(
    val title: String,
    val requirement: String,
    val guidelineUrl : String,
    val reward : Int
)
