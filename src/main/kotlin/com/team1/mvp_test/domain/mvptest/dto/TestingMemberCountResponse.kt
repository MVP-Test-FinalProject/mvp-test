package com.team1.mvp_test.domain.mvptest.dto

import com.team1.mvp_test.domain.mvptest.model.MvpTest

data class TestingMemberCountResponse(
    val testId: Long,
    val currentTestingMemberCount: Long,
) {
    companion object {
        fun from(test: MvpTest, count: Long): TestingMemberCountResponse {
            return TestingMemberCountResponse(
                testId = test.id!!,
                currentTestingMemberCount = count
            )
        }
    }
}
