package com.team1.mvp_test.domain.mvptest.dto

import com.team1.mvp_test.domain.mvptest.model.RecruitType
import java.time.LocalDate

data class UpdateMvpTestRequest(
    val mvpName: String,
    val recruitStartDate: LocalDate,
    val recruitEndDate: LocalDate,
    val testStartDate: LocalDate,
    val testEndDate: LocalDate,
    val mainImageUrl: String,
    val mvpInfo: String,
    val mvpUrl: String,
    val rewardBudget: Int,
    val requirementMinAge: Int?,
    val requirementMaxAge: Int?,
    val requirementSex: Boolean,
    val recruitType: RecruitType,
    val recruitNum: Long,
    val categories: List<String>
)