package com.team1.mvp_test.batch

import com.team1.mvp_test.batch.model.BatchStatus
import com.team1.mvp_test.batch.repository.BatchDataRepository
import com.team1.mvp_test.batch.repository.BatchJobDataRepository
import com.team1.mvp_test.batch.service.BatchService
import com.team1.mvp_test.domain.enterprise.model.Enterprise
import com.team1.mvp_test.domain.enterprise.model.EnterpriseState
import com.team1.mvp_test.domain.enterprise.repository.EnterpriseRepository
import com.team1.mvp_test.domain.member.model.*
import com.team1.mvp_test.domain.member.repository.MemberRepository
import com.team1.mvp_test.domain.member.repository.MemberTestRepository
import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.mvptest.model.MvpTestState
import com.team1.mvp_test.domain.mvptest.model.RecruitType
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import com.team1.mvp_test.domain.report.model.Report
import com.team1.mvp_test.domain.report.model.ReportState
import com.team1.mvp_test.domain.report.repository.ReportRepository
import com.team1.mvp_test.domain.step.model.Step
import com.team1.mvp_test.domain.step.repository.StepRepository
import com.team1.mvp_test.infra.querydsl.QueryDslConfig
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QueryDslConfig::class])
@ActiveProfiles("test")
class BatchServiceTest @Autowired constructor(
    private val mvpTestRepository: MvpTestRepository,
    private val memberTestRepository: MemberTestRepository,
    private val reportRepository: ReportRepository,
    private val batchDataRepository: BatchDataRepository,
    private val batchJobDataRepository: BatchJobDataRepository,
    private val enterprisesRepository: EnterpriseRepository,
    private val stepRepository: StepRepository,
    private val memberRepository: MemberRepository,

    ) {
    val batchService = BatchService(
        mvpTestRepository = mvpTestRepository,
        memberTestRepository = memberTestRepository,
        reportRepository = reportRepository,
        batchDataRepository = batchDataRepository,
        batchJobDataRepository = batchJobDataRepository
    )

    fun setup() {
        enterprisesRepository.saveAndFlush(ENTERPRISE)
        mvpTestRepository.saveAllAndFlush(MVP_TEST_LIST)
        stepRepository.saveAllAndFlush(TEST1_STEP_LIST)
        stepRepository.saveAllAndFlush(TEST2_STEP_LIST)
        stepRepository.saveAllAndFlush(TEST3_STEP_LIST)
        stepRepository.saveAllAndFlush(TEST4_STEP_LIST)
        stepRepository.saveAllAndFlush(TEST5_STEP_LIST)
        memberRepository.saveAllAndFlush(MEMBER_LIST)
        memberTestRepository.saveAllAndFlush(MEMBER_TEST_LIST)
        reportRepository.saveAllAndFlush(MEMBER_TEST_1_REPORT_LIST)
        reportRepository.saveAllAndFlush(MEMBER_TEST_2_REPORT_LIST)
        reportRepository.saveAllAndFlush(MEMBER_TEST_3_REPORT_LIST)
    }

    @Test
    fun `5개의 종료된 테스트에 대해서 리워드 지급 시 성공 케이스`() {
        // Given : 5개의 종료된 테스트에 대해서 리워드 지급 시
        setup()
        val todayDate = LocalDateTime.of(2024, 7, 15, 0, 0, 0)

        // When : provideRewardAllTests 호출 시
        batchService.provideRewardAllTests(todayDate)

        // Then : 성공적으로 작업을 종료한다
        // BatchData
        val batchData = batchDataRepository.findByIdOrNull(1L)
        batchData shouldNotBe null
        batchData!!.totalCount shouldBe 5
        batchData.failedCount shouldBe 0
        batchData.status shouldBe BatchStatus.COMPLETED

        // BatchJobData
        val batchJobs = batchJobDataRepository.findAllByBatchId(1L)
        batchJobs.size shouldBe 5
        batchJobs.forEach {
            it.status shouldBe BatchStatus.COMPLETED
        }

        // Member-point
        memberRepository.findByIdOrNull(1L)?.point shouldBe 3000
        memberRepository.findByIdOrNull(2L)?.point shouldBe 1000
        memberRepository.findByIdOrNull(3L)?.point shouldBe 3000
        memberRepository.findByIdOrNull(4L)?.point shouldBe 0
        memberRepository.findByIdOrNull(5L)?.point shouldBe 0

        // MvpTest settlement 완료 여부
        mvpTestRepository.findAll().forEach {
            it.settlementDate shouldNotBe null
        }

        mvpTestRepository.findAllUnsettledMvpTests(todayDate).size shouldBe 0

    }


    companion object {
        private val ENTERPRISE = Enterprise(
            id = 1L,
            email = "test@gmail.com",
            name = "testCorporation",
            ceoName = "testCEO",
            password = "testPassword",
            phoneNumber = "01011111111",
            state = EnterpriseState.APPROVED
        )
        private val MVP_TEST_LIST = listOf(
            MvpTest(
                id = 1L,
                enterpriseId = 1L,
                mvpName = "testMVP1",
                recruitStartDate = LocalDateTime.of(2024, 7, 10, 0, 0, 0),
                recruitEndDate = LocalDateTime.of(2024, 7, 11, 0, 0, 0),
                testStartDate = LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                testEndDate = LocalDateTime.of(2024, 7, 13, 0, 0, 0),
                mainImageUrl = "testMainImageUrl",
                mvpInfo = "testInfo",
                mvpUrl = "testUrl",
                rewardBudget = 1000000,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = 10,
                state = MvpTestState.APPROVED,
            ), MvpTest(
                id = 2L,
                enterpriseId = 1L,
                mvpName = "testMVP2",
                recruitStartDate = LocalDateTime.of(2024, 7, 10, 0, 0, 0),
                recruitEndDate = LocalDateTime.of(2024, 7, 11, 0, 0, 0),
                testStartDate = LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                testEndDate = LocalDateTime.of(2024, 7, 13, 0, 0, 0),
                mainImageUrl = "testMainImageUrl",
                mvpInfo = "testInfo",
                mvpUrl = "testUrl",
                rewardBudget = 1000000,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = 10,
                state = MvpTestState.APPROVED,
            ), MvpTest(
                id = 3L,
                enterpriseId = 1L,
                mvpName = "testMVP3",
                recruitStartDate = LocalDateTime.of(2024, 7, 10, 0, 0, 0),
                recruitEndDate = LocalDateTime.of(2024, 7, 11, 0, 0, 0),
                testStartDate = LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                testEndDate = LocalDateTime.of(2024, 7, 13, 0, 0, 0),
                mainImageUrl = "testMainImageUrl",
                mvpInfo = "testInfo",
                mvpUrl = "testUrl",
                rewardBudget = 1000000,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = 10,
                state = MvpTestState.APPROVED,
            ), MvpTest(
                id = 4L,
                enterpriseId = 1L,
                mvpName = "testMVP4",
                recruitStartDate = LocalDateTime.of(2024, 7, 10, 0, 0, 0),
                recruitEndDate = LocalDateTime.of(2024, 7, 11, 0, 0, 0),
                testStartDate = LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                testEndDate = LocalDateTime.of(2024, 7, 13, 0, 0, 0),
                mainImageUrl = "testMainImageUrl",
                mvpInfo = "testInfo",
                mvpUrl = "testUrl",
                rewardBudget = 1000000,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = 10,
                state = MvpTestState.APPROVED,
            ), MvpTest(
                id = 5L,
                enterpriseId = 1L,
                mvpName = "testMVP5",
                recruitStartDate = LocalDateTime.of(2024, 7, 10, 0, 0, 0),
                recruitEndDate = LocalDateTime.of(2024, 7, 11, 0, 0, 0),
                testStartDate = LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                testEndDate = LocalDateTime.of(2024, 7, 13, 0, 0, 0),
                mainImageUrl = "testMainImageUrl",
                mvpInfo = "testInfo",
                mvpUrl = "testUrl",
                rewardBudget = 1000000,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = 10,
                state = MvpTestState.APPROVED,
            )
        )
        private val TEST1_STEP_LIST = listOf(
            Step(
                id = 1L,
                title = "testStep1",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 1000,
                mvpTest = MVP_TEST_LIST[0]
            ),
            Step(
                id = 2L,
                title = "testStep2",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 2000,
                mvpTest = MVP_TEST_LIST[0]
            )
        )
        private val TEST2_STEP_LIST = listOf(
            Step(
                id = 3L,
                title = "testStep1",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 1000,
                mvpTest = MVP_TEST_LIST[1]
            ),
            Step(
                id = 4L,
                title = "testStep2",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 2000,
                mvpTest = MVP_TEST_LIST[1]
            )
        )
        private val TEST3_STEP_LIST = listOf(
            Step(
                id = 5L,
                title = "testStep1",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 1000,
                mvpTest = MVP_TEST_LIST[2]
            ),
            Step(
                id = 6L,
                title = "testStep2",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 2000,
                mvpTest = MVP_TEST_LIST[2]
            )
        )
        private val TEST4_STEP_LIST = listOf(
            Step(
                id = 7L,
                title = "testStep1",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 1000,
                mvpTest = MVP_TEST_LIST[3]
            ),
            Step(
                id = 8L,
                title = "testStep2",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 2000,
                mvpTest = MVP_TEST_LIST[3]
            )
        )
        private val TEST5_STEP_LIST = listOf(
            Step(
                id = 9L,
                title = "testStep1",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 1000,
                mvpTest = MVP_TEST_LIST[4]
            ),
            Step(
                id = 10L,
                title = "testStep2",
                requirement = "testRequirement",
                stepOrder = 1,
                reward = 2000,
                mvpTest = MVP_TEST_LIST[4]
            )
        )

        private val MEMBER_LIST = listOf(
            Member(
                id = 1L,
                name = "testMember1",
                email = "test@gmail.com",
                age = 20,
                sex = Sex.MALE,
                info = "testInfo",
                state = MemberState.ACTIVE
            ),
            Member(
                id = 2L,
                name = "testMember2",
                email = "test@gmail.com",
                age = 20,
                sex = Sex.MALE,
                info = "testInfo",
                state = MemberState.ACTIVE
            ), Member(
                id = 3L,
                name = "testMember3",
                email = "test@gmail.com",
                age = 20,
                sex = Sex.MALE,
                info = "testInfo",
                state = MemberState.ACTIVE
            ), Member(
                id = 4L,
                name = "testMember4",
                email = "test@gmail.com",
                age = 20,
                sex = Sex.MALE,
                info = "testInfo",
                state = MemberState.ACTIVE
            ), Member(
                id = 5L,
                name = "testMember5",
                email = "test@gmail.com",
                age = 20,
                sex = Sex.MALE,
                info = "testInfo",
                state = MemberState.ACTIVE
            )
        )

        private val MEMBER_TEST_LIST = listOf(
            MemberTest(
                id = 1L,
                member = MEMBER_LIST[0],
                test = MVP_TEST_LIST[0],
                state = MemberTestState.APPROVED
            ), MemberTest(
                id = 2L,
                member = MEMBER_LIST[1],
                test = MVP_TEST_LIST[1],
                state = MemberTestState.APPROVED
            ), MemberTest(
                id = 3L,
                member = MEMBER_LIST[2],
                test = MVP_TEST_LIST[2],
                state = MemberTestState.APPROVED
            ), MemberTest(
                id = 4L,
                member = MEMBER_LIST[3],
                test = MVP_TEST_LIST[3],
                state = MemberTestState.APPROVED
            ), MemberTest(
                id = 5L,
                member = MEMBER_LIST[4],
                test = MVP_TEST_LIST[4],
                state = MemberTestState.APPROVED
            )
        )

        private val MEMBER_TEST_1_REPORT_LIST = listOf(
            Report(
                title = "1:report1",
                body = "testBody",
                feedback = "testFeedback",
                state = ReportState.APPROVED,
                step = TEST1_STEP_LIST[0],
                memberTest = MEMBER_TEST_LIST[0]
            ),
            Report(
                title = "1:report2",
                body = "testBody",
                feedback = "testFeedback",
                state = ReportState.APPROVED,
                step = TEST1_STEP_LIST[1],
                memberTest = MEMBER_TEST_LIST[0]
            ),
        )
        private val MEMBER_TEST_2_REPORT_LIST = listOf(
            Report(
                title = "2:report1",
                body = "testBody",
                feedback = "testFeedback",
                state = ReportState.APPROVED,
                step = TEST2_STEP_LIST[0],
                memberTest = MEMBER_TEST_LIST[1]
            ),
        )

        private val MEMBER_TEST_3_REPORT_LIST = listOf(
            Report(
                title = "3:report1",
                body = "testBody",
                feedback = "testFeedback",
                state = ReportState.APPROVED,
                step = TEST3_STEP_LIST[0],
                memberTest = MEMBER_TEST_LIST[2]
            ),
            Report(
                title = "3:report2",
                body = "testBody",
                feedback = "testFeedback",
                state = ReportState.APPROVED,
                step = TEST3_STEP_LIST[1],
                memberTest = MEMBER_TEST_LIST[2]
            ),
        )
    }
}