package com.team1.mvp_test.domain.mvptest.dto.mvptest

import com.team1.mvp_test.domain.mvptest.model.MvpTest
import java.time.LocalDateTime

data class MvpTestResponse(
    val id: Long?,
    val enterpriseId: Long?,
    val mvpName: String,
    val recruitStartDate: LocalDateTime,
    val recruitEndDate: LocalDateTime,
    val testStartDate: LocalDateTime,
    val testEndDate: LocalDateTime,
    val mainImageUrl: String,
    val mvpInfo: String,
    val mvpUrl: String,
    val rewardBudget: Int,
    val requirementMinAge: Int?,
    val requirementMaxAge: Int?,
    val requirementSex: Boolean?,
    val recruitType: String,
    val recruitNum: Long,
    val category: List<String>?
) {
    companion object {
        fun from(mvpTest: MvpTest): MvpTestResponse {
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
                recruitType = mvpTest.recruitType.toString(),
                recruitNum = mvpTest.recruitNum,
                category = mvpTest.categories?.map { it.category.name }
            )

        }
    }
}