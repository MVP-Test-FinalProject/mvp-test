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
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest
class ConcurrencyControl {
    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var mvpTestRepository: MvpTestRepository

    @Autowired
    private lateinit var memberTestRepository: MemberTestRepository

    @Autowired
    private lateinit var mvpTestService: MvpTestService

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
                barrier.await()
                mvpTestService.applyToMvpTest(memberId = it.toLong() + 1L, test.id!!)
            }
        }
        executor.awaitTermination(5, TimeUnit.SECONDS)

        memberTestRepository.findAll().size shouldBe 50

    }

}