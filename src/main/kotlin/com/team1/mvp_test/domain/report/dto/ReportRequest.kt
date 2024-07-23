package com.team1.mvp_test.domain.report.dto

import jakarta.validation.constraints.Size

data class ReportRequest(
    @field:Size(min = 5, max = 50, message = "1")
    val title: String,

    @field:Size(min = 5, max = 500, message = "2")
    val body: String,

    val mediaUrl: List<String>,

    @field:Size(min = 5, max = 500, message = "3")
    val feedback: String,
)
