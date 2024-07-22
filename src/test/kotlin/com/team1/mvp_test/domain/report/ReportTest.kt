package com.team1.mvp_test.domain.report

import com.team1.mvp_test.common.exception.NoPermissionException
import com.team1.mvp_test.domain.member.model.Member
import com.team1.mvp_test.domain.member.model.MemberTest
import com.team1.mvp_test.domain.member.repository.MemberTestRepository
import com.team1.mvp_test.domain.mvptest.constant.RecruitType
import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.report.dto.ApproveReportRequest
import com.team1.mvp_test.domain.report.dto.UpdateReportRequest
import com.team1.mvp_test.domain.report.model.Report
import com.team1.mvp_test.domain.report.model.ReportMedia
import com.team1.mvp_test.domain.report.repository.ReportMediaRepository
import com.team1.mvp_test.domain.report.repository.ReportRepository
import com.team1.mvp_test.domain.report.service.ReportService
import com.team1.mvp_test.domain.step.model.Step
import com.team1.mvp_test.domain.step.repository.StepRepository
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime


class ReportTest : BehaviorSpec({

    val reportRepository = mockk<ReportRepository>(relaxed = true)
    val reportMediaRepository = mockk<ReportMediaRepository>(relaxed = true)
    val stepRepository = mockk<StepRepository>(relaxed = true)
    val memberTestRepository = mockk<MemberTestRepository>(relaxed = true)

    val reportService = ReportService(
        reportRepository = reportRepository,
        reportMediaRepository = reportMediaRepository,
        stepRepository = stepRepository,
        memberTestRepository = memberTestRepository
    )

    afterContainer {
        clearAllMocks()
    }

    Given("Report 작성 요건을 갖췄다면") {
        val stepId = 1L
        val memberId = 1L
        val request = UpdateReportRequest(
            title = "Test title",
            body = "Test body",
            feedback = "Test feedback",
            mediaUrl = listOf("test url1", "test url2")
        )
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.now().minusMonths(2),
            recruitEndDate = LocalDateTime.now().minusMonths(1),
            testStartDate = LocalDateTime.now().minusDays(10),
            testEndDate = LocalDateTime.now().plusDays(10),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 1000,
            requirementMinAge = 0,
            requirementMaxAge = 200,
            requirementSex = null,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 100L,
            categories = emptyList()
        )
        val step = Step(
            id = 1L,
            title = "Test step title",
            requirement = "Test requirement",
            guidelineUrl = "test guideline",
            stepOrder = 1,
            reward = 100,
            mvpTest = mvpTest
        )
        val member = Member(
            id = 1L,
            name = "test member name",
            email = "test@test.com",
            age = 20,
            sex = "test sex",
            info = "test info",
            signUpState = true
        )
        val memberTest = MemberTest(member = member, test = mvpTest)

        every { stepRepository.findByIdOrNull(stepId) } returns step
        every { memberTestRepository.findByMemberIdAndTestId(memberId, mvpTest.id) } returns memberTest
        every { reportRepository.findByStepAndMemberTest(step, memberTest) } returns null

        When("작성 시도하면") {
            Then("작성된다") {
                every { reportRepository.save(any()) } returns Report(
                    id = 1L,
                    title = "Test title",
                    body = "Test body",
                    feedback = "Test feedback",
                    isConfirmed = false,
                    reason = "Test report reason",
                    step = step,
                    memberTest = memberTest,
                    reportMedia = mutableListOf(ReportMedia(1L, "test url1"), ReportMedia(2L, "test url2") )
                )

                every { reportMediaRepository.save(any()) } returns ReportMedia(1L, "test url1")

                val response = reportService.createReport(stepId, request, memberId)

                response.title shouldBe "Test title"
                response.body shouldBe "Test body"
                response.feedback shouldBe "Test feedback"
                response.reportMedia.size shouldBe 2

                verify { reportRepository.save(any()) }
            }
        }
    }


    Given("Report 작성시 승인이") {
        val stepId = 1L
        val memberId = 1L
        val request = UpdateReportRequest(
            title = "Test title",
            body = "Test body",
            feedback = "Test feedback",
            mediaUrl = listOf("test url1", "test url2")
        )
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.now().minusMonths(2),
            recruitEndDate = LocalDateTime.now().minusMonths(1),
            testStartDate = LocalDateTime.now().minusDays(10),
            testEndDate = LocalDateTime.now().plusDays(10),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 1000,
            requirementMinAge = 0,
            requirementMaxAge = 200,
            requirementSex = null,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 100L,
            categories = emptyList()
        )
        val step = Step(
            id = 1L,
            title = "Test step title",
            requirement = "Test requirement",
            guidelineUrl = "test guideline",
            stepOrder = 1,
            reward = 100,
            mvpTest = mvpTest
        )
        val member = Member(
            id = 1L,
            name = "test member name",
            email = "test@test.com",
            age = 20,
            sex = "test sex",
            info = "test info",
            signUpState = true
        )
        val memberTest = MemberTest(member = member, test = mvpTest)


        When("해당 테스트의 승인 유저가 아니라면") {
            every { stepRepository.findByIdOrNull(stepId) } returns step
            every { memberTestRepository.findByMemberIdAndTestId(memberId, mvpTest.id) } returns null

            Then("NoPermissionException") {
                shouldThrowExactly<NoPermissionException> {
                    reportService.createReport(stepId, request, memberId)
                }
            }
        }
    }

    Given("Report 작성시 report 의 ") {
        When("테스트 기간이 지났다면") {
            val stepId = 1L
            val memberId = 1L
            val request = UpdateReportRequest(
                title = "Test title",
                body = "Test body",
                feedback = "Test feedback",
                mediaUrl = listOf("test url1", "test url2")
            )
            val mvpTest = MvpTest(
                id = 1L,
                enterpriseId = 1L,
                mvpName = "test mvp name",
                recruitStartDate = LocalDateTime.now().minusMonths(2),
                recruitEndDate = LocalDateTime.now().minusMonths(1),
                testStartDate = LocalDateTime.now().minusDays(10),
                testEndDate = LocalDateTime.now().minusDays(5),
                mainImageUrl = "test mvp main url",
                mvpInfo = "test mvp info",
                mvpUrl = "test mvp url",
                rewardBudget = 1000,
                requirementMinAge = 0,
                requirementMaxAge = 200,
                requirementSex = null,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = 100L,
                categories = emptyList()
            )
            val step = Step(
                id = 1L,
                title = "Test step title",
                requirement = "Test requirement",
                guidelineUrl = "test guideline",
                stepOrder = 1,
                reward = 100,
                mvpTest = mvpTest
            )
            val member = Member(
                id = 1L,
                name = "test member name",
                email = "test@test.com",
                age = 20,
                sex = "test sex",
                info = "test info",
                signUpState = true
            )
            val memberTest = MemberTest(member = member, test = mvpTest)

            every { stepRepository.findByIdOrNull(stepId) } returns step
            every { memberTestRepository.findByMemberIdAndTestId(memberId, mvpTest.id) } returns memberTest

            Then("IllegalArgumentException") {
                shouldThrowExactly<IllegalArgumentException> {
                    reportService.createReport(stepId, request, memberId)
                }
            }
        }
        When("테스트 기간 이전이라면") {
            val stepId = 1L
            val memberId = 1L
            val request = UpdateReportRequest(
                title = "Test title",
                body = "Test body",
                feedback = "Test feedback",
                mediaUrl = listOf<String>("test url1", "test url2")
            )
            val mvpTest = MvpTest(
                id = 1L,
                enterpriseId = 1L,
                mvpName = "test mvp name",
                recruitStartDate = LocalDateTime.now().minusMonths(2),
                recruitEndDate = LocalDateTime.now().minusMonths(1),
                testStartDate = LocalDateTime.now().plusDays(5),
                testEndDate = LocalDateTime.now().plusDays(10),
                mainImageUrl = "test mvp main url",
                mvpInfo = "test mvp info",
                mvpUrl = "test mvp url",
                rewardBudget = 1000,
                requirementMinAge = 0,
                requirementMaxAge = 200,
                requirementSex = null,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = 100L,
                categories = emptyList()
            )
            val step = Step(
                id = 1L,
                title = "Test step title",
                requirement = "Test requirement",
                guidelineUrl = "test guideline",
                stepOrder = 1,
                reward = 100,
                mvpTest = mvpTest
            )
            val member = Member(
                id = 1L,
                name = "test member name",
                email = "test@test.com",
                age = 20,
                sex = "test sex",
                info = "test info",
                signUpState = true
            )
            val memberTest = MemberTest(member = member, test = mvpTest)

            every { stepRepository.findByIdOrNull(stepId) } returns step
            every { memberTestRepository.findByMemberIdAndTestId(memberId, mvpTest.id) } returns memberTest

            Then("IllegalArgumentException") {
                shouldThrowExactly<IllegalArgumentException> {
                    reportService.createReport(stepId, request, memberId)
                }
            }
        }
    }

    Given("Report 작성시 해당 STEP 에") {
        val stepId = 1L
        val memberId = 1L
        val request = UpdateReportRequest(
            title = "Test title",
            body = "Test body",
            feedback = "Test feedback",
            mediaUrl = listOf("test url1", "test url2")
        )
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.now().minusMonths(2),
            recruitEndDate = LocalDateTime.now().minusMonths(1),
            testStartDate = LocalDateTime.now().minusDays(10),
            testEndDate = LocalDateTime.now().plusDays(10),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 1000,
            requirementMinAge = 0,
            requirementMaxAge = 200,
            requirementSex = null,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 100L,
            categories = emptyList()
        )
        val step = Step(
            id = 1L,
            title = "Test step title",
            requirement = "Test requirement",
            guidelineUrl = "test guideline",
            stepOrder = 1,
            reward = 100,
            mvpTest = mvpTest
        )
        val member = Member(
            id = 1L,
            name = "test member name",
            email = "test@test.com",
            age = 20,
            sex = "test sex",
            info = "test info",
            signUpState = true
        )
        val memberTest = MemberTest(member = member, test = mvpTest)

        every { stepRepository.findByIdOrNull(stepId) } returns step
        every { memberTestRepository.findByMemberIdAndTestId(memberId, mvpTest.id) } returns memberTest

        When("이미 Report 를 작성한 상태라면") {
            val report = Report(
                id = 1L,
                title = "Test report title",
                body = "Test report body",
                feedback = "Test feedback",
                isConfirmed = false,
                reason = "Test report reason",
                step = step,
                memberTest = memberTest,
            )

            every { reportRepository.findByStepAndMemberTest(step, memberTest) } returns report

            Then("IllegalArgumentException") {
                shouldThrowExactly<IllegalArgumentException> {
                    reportService.createReport(stepId, request, memberId)
                }
            }
        }
    }

    Given("Report 수정시 해당 Report 의 작성자 ID 가") {
        val stepId = 1L
        val memberId = 1L
        val request = UpdateReportRequest(
            title = "Test title",
            body = "Test body",
            feedback = "Test feedback",
            mediaUrl = listOf("test url1", "test url2")
        )
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.now().minusMonths(2),
            recruitEndDate = LocalDateTime.now().minusMonths(1),
            testStartDate = LocalDateTime.now().minusDays(10),
            testEndDate = LocalDateTime.now().plusDays(10),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 1000,
            requirementMinAge = 0,
            requirementMaxAge = 200,
            requirementSex = null,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 100L,
            categories = emptyList()
        )
        val step = Step(
            id = 1L,
            title = "Test step title",
            requirement = "Test requirement",
            guidelineUrl = "test guideline",
            stepOrder = 1,
            reward = 100,
            mvpTest = mvpTest
        )

        When("자신의 ID와 일치하지 않는다면") {
            val member = Member(
                id = 2L,
                name = "test member name",
                email = "test@test.com",
                age = 20,
                sex = "test sex",
                info = "test info",
                signUpState = true
            )
            val memberTest = MemberTest(member = member, test = mvpTest)

            every { stepRepository.findByIdOrNull(stepId) } returns step
            every { memberTestRepository.findByMemberIdAndTestId(memberId, mvpTest.id) } returns memberTest

            val reportId = 1L
            val report = Report(
                id = 1L,
                title = "Test report title",
                body = "Test report body",
                feedback = "Test feedback",
                isConfirmed = false,
                reason = "Test report reason",
                step = step,
                memberTest = memberTest,
            )

            every { reportRepository.findByIdOrNull(reportId) } returns report

            Then("IllegalStateException") {
                shouldThrowExactly<IllegalStateException> {
                    reportService.updateReport(reportId, request, memberId)
                }
            }
        }
    }

    Given("Report 삭제시 해당 report 작성자의 ID가 ") {
        val stepId = 1L
        val memberId = 1L
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.now().minusMonths(2),
            recruitEndDate = LocalDateTime.now().minusMonths(1),
            testStartDate = LocalDateTime.now().minusDays(10),
            testEndDate = LocalDateTime.now().plusDays(10),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 1000,
            requirementMinAge = 0,
            requirementMaxAge = 200,
            requirementSex = null,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 100L,
            categories = emptyList()
        )
        val step = Step(
            id = 1L,
            title = "Test step title",
            requirement = "Test requirement",
            guidelineUrl = "test guideline",
            stepOrder = 1,
            reward = 100,
            mvpTest = mvpTest
        )
        When("자신의 ID 와 일치하지 않는다면") {
            val member = Member(
                id = 2L,
                name = "test member name",
                email = "test@test.com",
                age = 20,
                sex = "test sex",
                info = "test info",
                signUpState = true
            )
            val memberTest = MemberTest(member = member, test = mvpTest)

            every { stepRepository.findByIdOrNull(stepId) } returns step
            every { memberTestRepository.findByMemberIdAndTestId(memberId, mvpTest.id) } returns memberTest

            val reportId = 1L
            val report = Report(
                id = 1L,
                title = "Test report title",
                body = "Test report body",
                feedback = "Test feedback",
                isConfirmed = false,
                reason = "Test report reason",
                step = step,
                memberTest = memberTest,
            )

            every { reportRepository.findByIdOrNull(reportId) } returns report

            Then("IllegalStateException") {
                shouldThrowExactly<IllegalStateException> {
                    reportService.deleteReport(reportId, memberId)
                }
            }
        }
    }

    Given("Report 가 이미 승인된 상태라면") {
        val stepId = 1L
        val memberId = 1L
        val request = UpdateReportRequest(
            title = "Test title",
            body = "Test body",
            feedback = "Test feedback",
            mediaUrl = listOf("test url1", "test url2")
        )
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.now().minusMonths(2),
            recruitEndDate = LocalDateTime.now().minusMonths(1),
            testStartDate = LocalDateTime.now().minusDays(10),
            testEndDate = LocalDateTime.now().plusDays(10),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 1000,
            requirementMinAge = 0,
            requirementMaxAge = 200,
            requirementSex = null,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 100L,
            categories = emptyList()
        )
        val step = Step(
            id = 1L,
            title = "Test step title",
            requirement = "Test requirement",
            guidelineUrl = "test guideline",
            stepOrder = 1,
            reward = 100,
            mvpTest = mvpTest
        )
        val member = Member(
            id = 1L,
            name = "test member name",
            email = "test@test.com",
            age = 20,
            sex = "test sex",
            info = "test info",
            signUpState = true
        )
        val memberTest = MemberTest(member = member, test = mvpTest)

        every { stepRepository.findByIdOrNull(stepId) } returns step
        every { memberTestRepository.findByMemberIdAndTestId(memberId, mvpTest.id) } returns memberTest

        When("수정시") {
            val reportId = 1L
            val report = Report(
                id = 1L,
                title = "Test report title",
                body = "Test report body",
                feedback = "Test feedback",
                isConfirmed = true,
                reason = "Test report reason",
                step = step,
                memberTest = memberTest,
            )

            every { reportRepository.findByIdOrNull(reportId) } returns report

            Then("IllegalArgumentException") {
                shouldThrowExactly<IllegalArgumentException> {
                    reportService.updateReport(reportId, request, memberId)
                }
            }
        }
        When("삭제시") {
            val reportId = 1L
            val report = Report(
                id = 1L,
                title = "Test report title",
                body = "Test report body",
                feedback = "Test feedback",
                isConfirmed = true,
                reason = "Test report reason",
                step = step,
                memberTest = memberTest,
            )

            every { reportRepository.findByIdOrNull(reportId) } returns report

            Then("IllegalArgumentException") {
                shouldThrowExactly<IllegalArgumentException> {
                    reportService.deleteReport(reportId, memberId)
                }
            }
        }
    }

    Given("UpdateReportRequest 의 ReportMedia 가 10개가 넘으면") {
        val stepId = 1L
        val memberId = 1L
        val request = UpdateReportRequest(
            title = "Test title",
            body = "Test body",
            feedback = "Test feedback",
            mediaUrl = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        )
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.now().minusMonths(2),
            recruitEndDate = LocalDateTime.now().minusMonths(1),
            testStartDate = LocalDateTime.now().minusDays(10),
            testEndDate = LocalDateTime.now().plusDays(10),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 1000,
            requirementMinAge = 0,
            requirementMaxAge = 200,
            requirementSex = null,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 100L,
            categories = emptyList()
        )
        val step = Step(
            id = 1L,
            title = "Test step title",
            requirement = "Test requirement",
            guidelineUrl = "test guideline",
            stepOrder = 1,
            reward = 100,
            mvpTest = mvpTest
        )
        val member = Member(
            id = 1L,
            name = "test member name",
            email = "test@test.com",
            age = 20,
            sex = "test sex",
            info = "test info",
            signUpState = true
        )
        val memberTest = MemberTest(member = member, test = mvpTest)

        every { stepRepository.findByIdOrNull(stepId) } returns step
        every { memberTestRepository.findByMemberIdAndTestId(memberId, mvpTest.id) } returns memberTest
        When("작성시") {
            Then("IllegalArgumentException") {
                shouldThrowExactly<IllegalArgumentException> {
                    reportService.createReport(stepId, request, memberId)
                }
            }
        }
        When("수정시") {
            val reportId = 1L
            val report = Report(
                id = 1L,
                title = "Test report title",
                body = "Test report body",
                feedback = "Test feedback",
                isConfirmed = true,
                reason = "Test report reason",
                step = step,
                memberTest = memberTest,
            )

            every { reportRepository.findByIdOrNull(reportId) } returns report

            Then("IllegalArgumentException") {
                shouldThrowExactly<IllegalArgumentException> {
                    reportService.updateReport(stepId, request, memberId)
                }
            }
        }
    }

    Given("Report 승인시 자신의 ID 가 MvpTest 작성자의 ID와") {
        val stepId = 1L
        val request = ApproveReportRequest(
            reason = "Test reason",
            isConfirmed = true,
        )
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.now().minusMonths(2),
            recruitEndDate = LocalDateTime.now().minusMonths(1),
            testStartDate = LocalDateTime.now().minusDays(10),
            testEndDate = LocalDateTime.now().plusDays(10),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 1000,
            requirementMinAge = 0,
            requirementMaxAge = 200,
            requirementSex = null,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 100L,
            categories = emptyList()
        )
        val step = Step(
            id = 1L,
            title = "Test step title",
            requirement = "Test requirement",
            guidelineUrl = "test guideline",
            stepOrder = 1,
            reward = 100,
            mvpTest = mvpTest
        )
        val member = Member(
            id = 1L,
            name = "test member name",
            email = "test@test.com",
            age = 20,
            sex = "test sex",
            info = "test info",
            signUpState = true
        )
        val memberTest = MemberTest(member = member, test = mvpTest)

        val reportId = 1L
        val report = Report(
            id = 1L,
            title = "Test report title",
            body = "Test report body",
            feedback = "Test feedback",
            isConfirmed = false,
            reason = "Test report reason",
            step = step,
            memberTest = memberTest,
        )

        every { reportRepository.findByIdOrNull(reportId) } returns report

        When("해당 테스트의 작성 회사 ID와 일치하지 않는다면") {
            Then("IllegalStateException") {
                val enterpriseId = 2L

                shouldThrowExactly<IllegalStateException> {
                    reportService.approveReport(reportId, request, enterpriseId)
                }
            }
        }
    }

    Given("Report 승인시 자신의 ID와") {
        val stepId = 1L
        val request = ApproveReportRequest(
            reason = "Test reason",
            isConfirmed = true,
        )
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
            mvpName = "test mvp name",
            recruitStartDate = LocalDateTime.now().minusMonths(2),
            recruitEndDate = LocalDateTime.now().minusMonths(1),
            testStartDate = LocalDateTime.now().minusDays(10),
            testEndDate = LocalDateTime.now().plusDays(10),
            mainImageUrl = "test mvp main url",
            mvpInfo = "test mvp info",
            mvpUrl = "test mvp url",
            rewardBudget = 1000,
            requirementMinAge = 0,
            requirementMaxAge = 200,
            requirementSex = null,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 100L,
            categories = emptyList()
        )
        val step = Step(
            id = 1L,
            title = "Test step title",
            requirement = "Test requirement",
            guidelineUrl = "test guideline",
            stepOrder = 1,
            reward = 100,
            mvpTest = mvpTest
        )
        val member = Member(
            id = 1L,
            name = "test member name",
            email = "test@test.com",
            age = 20,
            sex = "test sex",
            info = "test info",
            signUpState = true
        )
        val memberTest = MemberTest(member = member, test = mvpTest)

        val reportId = 1L
        val report = Report(
            id = 1L,
            title = "Test report title",
            body = "Test report body",
            feedback = "Test feedback",
            isConfirmed = false,
            reason = "Test report reason",
            step = step,
            memberTest = memberTest,
        )

        every { reportRepository.findByIdOrNull(reportId) } returns report

        When("해당 테스트의 작성 회사 ID와 일치한다면") {
            Then("승인된다") {
                val enterpriseId = 1L

                val slot = slot<Report>()
                every { reportRepository.save(capture(slot)) } answers { firstArg() }

                val response = reportService.approveReport(reportId, request, enterpriseId)

                response.isConfirmed shouldBe true
                response.reason shouldBe "Test reason"
                verify { reportRepository.save(any()) }
            }
        }
    }

})