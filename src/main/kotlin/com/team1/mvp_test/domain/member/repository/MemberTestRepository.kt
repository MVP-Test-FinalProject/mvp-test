package com.team1.mvp_test.domain.member.repository

import com.team1.mvp_test.domain.member.model.MemberTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberTestRepository : JpaRepository<MemberTest, Long> {
    fun findByMemberIdAndTestId(memberId: Long, testId: Long): MemberTest?

    @Query("select count(m) from MemberTest m where m.test.id = :testId")
    fun countAllTestingMembers(@Param("testId") testId: Long): Long

}