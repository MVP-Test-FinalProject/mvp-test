package com.team1.mvp_test.batch.service

import com.team1.mvp_test.domain.member.repository.MemberTestRepository
import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import com.team1.mvp_test.domain.report.model.ReportState
import com.team1.mvp_test.domain.report.repository.ReportRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BatchService(
    private val mvpTestRepository: MvpTestRepository,
    private val memberTestRepository: MemberTestRepository,
    private val reportRepository: ReportRepository,
) {

    @Transactional
    fun provideRewardAllTests(todayDate: LocalDateTime) {
        mvpTestRepository.findAllUnsettledMvpTests(todayDate)
            .forEach { provideRewardEachTest(it, todayDate) }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun provideRewardEachTest(test: MvpTest, todayDate: LocalDateTime) {
        val memberTests = memberTestRepository.findAllByTestId(test.id!!)
        for (memberTest in memberTests) {
            var reward = 0
            reportRepository.findAllByMemberTest(memberTest).forEach {
                if (it.state == ReportState.APPROVED) reward += it.step.reward
            }
            memberTest.member.settleReward(reward)
        }
        test.settlementDate = todayDate
    }
}