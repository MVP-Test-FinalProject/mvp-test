package com.team1.mvp_test.domain.member.repository

import com.team1.mvp_test.domain.member.model.MemberTest
import com.team1.mvp_test.domain.member.model.MemberTestState
import org.springframework.data.jpa.repository.JpaRepository

interface MemberTestRepository : JpaRepository<MemberTest, Long> {
    fun findByMemberIdAndTestId(memberId: Long, testId: Long): MemberTest?
    fun countByTestIdAndState(testId: Long, state: MemberTestState): Long
    fun findAllByTestId(testId: Long): List<MemberTest>
}