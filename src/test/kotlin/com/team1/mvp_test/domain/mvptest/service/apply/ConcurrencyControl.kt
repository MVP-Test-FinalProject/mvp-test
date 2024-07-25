package com.team1.mvp_test.domain.mvptest.service.apply

import com.team1.mvp_test.domain.member.model.Member
import com.team1.mvp_test.domain.member.model.Sex
import com.team1.mvp_test.domain.member.repository.MemberRepository
import com.team1.mvp_test.domain.member.repository.MemberTestRepository
import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.mvptest.model.MvpTestState
import com.team1.mvp_test.domain.mvptest.model.RecruitType
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import com.team1.mvp_test.domain.mvptest.service.MvpTestService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDateTime
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@DataJpaTest(showSql = false)
class ConcurrencyControl @Autowired constructor(
    private var memberRepository: MemberRepository,
    private var mvpTestRepository: MvpTestRepository,
    private var memberTestRepository: MemberTestRepository
) {
    private var mvpTestService = MvpTestService(memberTestRepository)

    @BeforeEach
    fun setup() {
        memberRepository.deleteAll()
        mvpTestRepository.deleteAll()
        memberTestRepository.deleteAll()
    }

    @Test
    fun testConcurrencyControl() {
        val threadCount = 100

        val executor = Executors.newFixedThreadPool(threadCount)
        val barrier = CyclicBarrier(threadCount)

        repeat(threadCount) {
            memberRepository.save(Member(id = it.toLong() + 1L, email = "test@test.test"))
        }

        val test = MvpTest(
            id = 1,
            enterpriseId = 1,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.of(2024, 5, 1, 12, 0),
            recruitEndDate = LocalDateTime.of(2025, 5, 5, 12, 0),
            testStartDate = LocalDateTime.of(2025, 5, 10, 12, 0),
            testEndDate = LocalDateTime.of(2025, 5, 15, 12, 0),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 10000,
            requirementMinAge = null,
            requirementMaxAge = null,
            requirementSex = Sex.MALE,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        ).let { mvpTestRepository.save(it) }

        repeat(threadCount) {
            executor.execute {
                try {
                    barrier.await()
                    mvpTestService.applyToMvpTest(memberId = it.toLong() + 1L, test.id!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        executor.awaitTermination(5, TimeUnit.SECONDS)

        assert(memberTestRepository.findAll().size == 50)
    }
}