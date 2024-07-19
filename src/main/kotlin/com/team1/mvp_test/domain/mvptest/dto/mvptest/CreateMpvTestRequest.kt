package com.team1.mvp_test.domain.mvptest.dto.mvptest

import java.time.LocalDateTime

data class CreateMpvTestRequest(
        val mvpName : String,
        val mvpType : String,
        val recruitStartDate : LocalDateTime,
        val recruitEndDate : LocalDateTime,
        val testStartDate : LocalDateTime,
        val testEndDate : LocalDateTime,
        val mainImageUrl : String,
        val mvpInfo : String,
        val rewardBudget : Int,
        val requirementMinAge : Int?,
        val requirementMaxAge : Int?,
        val requirementSex : Boolean,
        val recruitType : String,
        val recruitNum : Long,
        val category : List<String>
)