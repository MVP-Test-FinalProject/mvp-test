package com.team1.mvp_test.domain.mvptest.dto

import java.time.LocalDateTime

open class MvpTestRequest(
    val recruitStartDate: LocalDateTime,
    val recruitEndDate: LocalDateTime,
    val testStartDate: LocalDateTime,
    val testEndDate: LocalDateTime,
    val requirementMinAge: Int?,
    val requirementMaxAge: Int?,
)