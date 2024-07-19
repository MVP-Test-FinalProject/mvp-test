package com.team1.mvp_test.domain.mvptest.dto

import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.mvptest.model.RecruitType
import java.time.LocalDate

data class MvpTestResponse(
    val id: Long?,
    val enterpriseId: Long?,
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
    val requirementSex: Boolean?,
    val recruitType: RecruitType,
    val recruitNum: Long,
    val categories: List<String>
) {
    companion object {
        fun from(mvpTest: MvpTest, categories: List<String>): MvpTestResponse {
            return MvpTestResponse(
                id = mvpTest.id,
                enterpriseId = mvpTest.enterpriseId,
                mvpName = mvpTest.mvpName,
                recruitStartDate = mvpTest.recruitStartDate,
                recruitEndDate = mvpTest.recruitEndDate,
                testStartDate = mvpTest.testStartDate,
                testEndDate = mvpTest.testEndDate,
                mainImageUrl = mvpTest.mainImageUrl,
                mvpInfo = mvpTest.mvpInfo,
                mvpUrl = mvpTest.mvpUrl,
                rewardBudget = mvpTest.rewardBudget,
                requirementMinAge = mvpTest.requirementMinAge,
                requirementMaxAge = mvpTest.requirementMaxAge,
                requirementSex = mvpTest.requirementSex,
                recruitType = mvpTest.recruitType,
                recruitNum = mvpTest.recruitNum,
                categories = categories
            )
        }
    }
}