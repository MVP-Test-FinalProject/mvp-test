package com.team1.mvp_test.domain.report.repository

import com.team1.mvp_test.domain.member.model.MemberTest
import com.team1.mvp_test.domain.report.model.Report
import com.team1.mvp_test.domain.step.model.Step
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository: JpaRepository<Report, Long> {

    fun findByStepAndMemberTest(step: Step, memberTest: MemberTest): Report?

}