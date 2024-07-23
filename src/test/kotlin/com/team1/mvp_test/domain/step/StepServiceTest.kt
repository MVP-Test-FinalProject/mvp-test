package com.team1.mvp_test.domain.step

import com.team1.mvp_test.common.exception.ModelNotFoundException
import com.team1.mvp_test.common.exception.NoPermissionException
import com.team1.mvp_test.domain.member.model.Sex
import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.mvptest.model.MvpTestState
import com.team1.mvp_test.domain.mvptest.model.RecruitType
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import com.team1.mvp_test.domain.step.dto.CreateStepRequest
import com.team1.mvp_test.domain.step.dto.UpdateStepRequest
import com.team1.mvp_test.domain.step.model.Step
import com.team1.mvp_test.domain.step.repository.StepRepository
import com.team1.mvp_test.domain.step.service.StepService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime


class StepServiceTest : BehaviorSpec({

    val stepRepository = mockk<StepRepository>()
    val mvpTestRepository = mockk<MvpTestRepository>()

    val stepService = StepService(
        stepRepository = stepRepository,
        mvpTestRepository = mvpTestRepository,
    )

    Given("존재하지 않는 테스트에 대해서") {
        every { mvpTestRepository.findByIdOrNull(any()) } returns null
        When("createStep 실행 시") {
            Then("ModelNotFoundException 예외를 던진다") {
                shouldThrow<ModelNotFoundException> {
                    stepService.createStep(ENTERPRISE_ID, TEST_ID, createStepRequest)
                }
            }
        }
    }

    Given("권한이 없는 테스트에 대해서") {
        every { mvpTestRepository.findByIdOrNull(any()) } returns mvpTest
        every { stepRepository.findByIdOrNull(any()) } returns step
        val otherEnterpriseId = 2L
        When("createStep 실행 시") {
            Then("NoPermissionException 예외를 던진다") {
                shouldThrow<NoPermissionException> {
                    stepService.createStep(otherEnterpriseId, TEST_ID, createStepRequest)
                }
            }
        }
        When("updateStep 실행 시") {
            Then("NoPermissionException 예외를 던진다") {
                shouldThrow<NoPermissionException> {
                    stepService.updateStep(otherEnterpriseId, STEP_ID, updateStepRequest)
                }
            }
        }
        When("deleteStep 실행 시") {
            Then("NoPermissionException 예외를 던진다") {
                shouldThrow<NoPermissionException> {
                    stepService.deleteStep(otherEnterpriseId, STEP_ID)
                }
            }
        }
    }

    Given("존재하지 않는 STEP에 대해서") {
        every { stepRepository.findByIdOrNull(any()) } returns null
        When("updateStep 실행 시") {
            Then("ModelNotFoundException 예외를 던진다") {
                shouldThrow<ModelNotFoundException> {
                    stepService.updateStep(ENTERPRISE_ID, STEP_ID, updateStepRequest)
                }
            }
        }
        When("deleteStep 실행 시") {
            Then("ModelNotFoundException 예외를 던진다") {
                shouldThrow<ModelNotFoundException> {
                    stepService.deleteStep(ENTERPRISE_ID, STEP_ID)
                }
            }
        }
    }

}) {
    companion object {
        private const val TEST_ID = 1L
        private const val ENTERPRISE_ID = 1L
        private const val STEP_ID = 1L
        private val mvpTest = MvpTest(
            id = TEST_ID,
            enterpriseId = ENTERPRISE_ID,
            mvpName = "string",
            recruitStartDate = LocalDateTime.of(2025, 5, 1, 12, 0),
            recruitEndDate = LocalDateTime.of(2025, 5, 5, 12, 0),
            testStartDate = LocalDateTime.of(2025, 5, 10, 12, 0),
            testEndDate = LocalDateTime.of(2025, 5, 15, 12, 0),
            mainImageUrl = "string",
            mvpInfo = "string",
            mvpUrl = "string",
            rewardBudget = 100000,
            requirementMinAge = 15,
            requirementMaxAge = 60,
            requirementSex = Sex.MALE,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        )

        private val createStepRequest = CreateStepRequest(
            title = "title",
            requirement = "requirement",
            guidelineUrl = "string",
            reward = 100
        )
        private val updateStepRequest = UpdateStepRequest(
            title = "test_title",
            requirement = "test_requirement",
            guidelineUrl = "test_url",
        )
        private const val MAX_ORDER = 0

        private val step = Step(
            id = STEP_ID,
            title = createStepRequest.title,
            requirement = createStepRequest.requirement,
            guidelineUrl = createStepRequest.guidelineUrl,
            reward = createStepRequest.reward,
            stepOrder = MAX_ORDER + 1,
            mvpTest = mvpTest
        )
    }
}






