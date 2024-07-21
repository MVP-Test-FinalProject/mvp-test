package com.team1.mvp_test.domain.report.dto

data class UpdateReportRequest(
    val title: String,
    val body: String,
    val mediaUrl: List<String>,
    val feedback: String,
)
