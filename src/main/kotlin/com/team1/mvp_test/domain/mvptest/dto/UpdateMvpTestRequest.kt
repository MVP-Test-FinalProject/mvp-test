package com.team1.mvp_test.domain.mvptest.dto

import com.team1.mvp_test.domain.member.model.Sex
import com.team1.mvp_test.domain.mvptest.model.RecruitType
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class UpdateMvpTestRequest(
    @field:NotBlank
    @field:Size(max = 50)
    val mvpName: String,

    val recruitStartDate: LocalDateTime,
    val recruitEndDate: LocalDateTime,
    val testStartDate: LocalDateTime,
    val testEndDate: LocalDateTime,

    @field:NotBlank
    val mainImageUrl: String,

    @field:NotBlank
    val mvpInfo: String,

    @field:NotBlank
    val mvpUrl: String,

    @field:Min(10000)
    val rewardBudget: Int,

    @field:Min(15)
    @field:Max(99)
    val requirementMinAge: Int?,

    @field:Max(100)
    val requirementMaxAge: Int?,

    val requirementSex: Sex,
    val recruitType: RecruitType,

    @field:Min(1)
    val recruitNum: Int,

    val categories: List<String>
)