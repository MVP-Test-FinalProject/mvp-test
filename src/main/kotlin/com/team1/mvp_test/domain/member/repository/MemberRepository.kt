package com.team1.mvp_test.domain.member.repository

import com.team1.mvp_test.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun existsByEmail(email: String): Boolean
}