package com.team1.mvp_test.domain.mvptest.dto

import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.mvptest.model.MvpTestState
import com.team1.mvp_test.domain.mvptest.model.RecruitType
import java.time.LocalDateTime

data class CreateMvpTestRequest(
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
    val recruitType: RecruitType,
    val recruitNum: Long,
    val categories: List<String>
) {
    fun toMvpTest(enterpriseId: Long): MvpTest {
        return MvpTest(
            enterpriseId = enterpriseId,
            mvpName = mvpName,
            recruitStartDate = recruitStartDate,
            recruitEndDate = recruitEndDate,
            testStartDate = testStartDate,
            testEndDate = testEndDate,
            mainImageUrl = mainImageUrl,
            mvpInfo = mvpInfo,
            mvpUrl = mvpUrl,
            rewardBudget = rewardBudget,
            requirementMinAge = requirementMinAge,
            requirementMaxAge = requirementMaxAge,
            requirementSex = requirementSex,
            recruitType = recruitType,
            recruitNum = recruitNum,
            state = MvpTestState.PENDING
        )
    }
}