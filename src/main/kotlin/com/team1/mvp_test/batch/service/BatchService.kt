package com.team1.mvp_test.batch.service

import com.team1.mvp_test.batch.model.BatchData
import com.team1.mvp_test.batch.model.BatchJobData
import com.team1.mvp_test.batch.model.BatchStatus
import com.team1.mvp_test.batch.repository.BatchDataRepository
import com.team1.mvp_test.batch.repository.BatchJobDataRepository
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
    private val batchDataRepository: BatchDataRepository,
    private val batchJobDataRepository: BatchJobDataRepository
) {

    @Transactional
    fun provideRewardAllTests(todayDate: LocalDateTime) {
        val startTime = LocalDateTime.now()
        val mvpTests = mvpTestRepository.findAllUnsettledMvpTests(todayDate)
        var failedCount = 0
        val totalCount = mvpTests.size
        val batchData = BatchData(totalCount = totalCount)
            .let { batchDataRepository.saveAndFlush(it) }
        for (mvpTest in mvpTests) {
            runCatching { provideRewardEachTest(mvpTest, todayDate) }
                .onSuccess {
                    batchJobDataRepository.save(
                        BatchJobData(
                            status = BatchStatus.COMPLETED,
                            testId = mvpTest.id!!,
                            batch = batchData
                        )
                    )
                }
                .onFailure {
                    failedCount += 1
                    batchJobDataRepository.save(
                        BatchJobData(
                            status = BatchStatus.FAILED,
                            testId = mvpTest.id!!,
                            batch = batchData
                        )
                    )
                }
        }
        batchData.apply {
            this.status = if (failedCount == 0) BatchStatus.COMPLETED else BatchStatus.FAILED
            this.failedCount = failedCount
            this.startTime = startTime
            this.endTime = LocalDateTime.now()
        }.let { batchDataRepository.save(it) }
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