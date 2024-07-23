package com.team1.mvp_test.step

import com.team1.mvp_test.common.exception.ModelNotFoundException
import com.team1.mvp_test.common.exception.NoPermissionException
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
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime


class StepServiceTest {

    private val stepRepository = mockk<StepRepository>()
    private val mvpTestRepository = mockk<MvpTestRepository>()
    private val stepService = StepService(stepRepository, mvpTestRepository)

    @Test
    fun `createStep - 성공 케이스 `() {
        //given
        val testId = 1L
        val enterpriseId = 1L
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
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
            requirementSex = true,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        )

        val request = CreateStepRequest(
            title = "title",
            requirement = "requirement",
            guidelineUrl = "string",
            reward = 100
        )

        val maxOrder = 0

        val step = Step(
            id = 1L,
            title = request.title,
            requirement = request.requirement,
            guidelineUrl = request.guidelineUrl,
            reward = request.reward,
            stepOrder = maxOrder + 1,
            mvpTest = mvpTest
        )

        every { mvpTestRepository.findByIdOrNull(testId) } returns mvpTest
        every { stepRepository.findMaxOrderByTestId(testId) } returns maxOrder
        every { stepRepository.save(any()) } returns step

        //when
        val result = stepService.createStep(enterpriseId, testId, request)

        //then
        result.title shouldBe "title"
        result.requirement shouldBe "requirement"
        result.guidelineUrl shouldBe "string"
        result.reward shouldBe 100
        verify { stepRepository.save(any()) }
    }

    @Test
    fun `updateStepById - 성공 케이스`() {
        //given
        val stepId = 1L
        val testId = 1L
        val enterpriseId = 1L
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
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
            requirementSex = true,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        )

        val step = Step(
            id = 1L,
            title = "title",
            requirement = "requirement",
            guidelineUrl = "string",
            reward = 100,
            stepOrder = 1,
            mvpTest = mvpTest
        )

        val request = UpdateStepRequest(
            title = "test_title",
            requirement = "test_requirement",
            guidelineUrl = "test_url",
        )
        every { mvpTestRepository.findByIdOrNull(testId) } returns mvpTest
        every { stepRepository.findByIdOrNull(stepId) } returns step

        //when
        val result = stepService.updateStepById(enterpriseId, stepId, request)

        //then
        result.title shouldBe "test_title"
        result.requirement shouldBe "test_requirement"
        result.guidelineUrl shouldBe "test_url"
    }

    @Test
    fun `deleteStepById - 성공 케이스`() {
        // given
        val stepId = 1L
        val testId = 1L
        val enterpriseId = 1L
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
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
            requirementSex = true,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        )

        val step = Step(
            id = 1L,
            title = "title",
            requirement = "requirement",
            guidelineUrl = "string",
            reward = 100,
            stepOrder = 1,
            mvpTest = mvpTest
        )

        every { mvpTestRepository.findByIdOrNull(testId) } returns mvpTest
        every { stepRepository.findByIdOrNull(stepId) } returns step
        every { stepRepository.delete(any()) } just Runs

        // when
        stepService.deleteStepById(enterpriseId, stepId)

        // then
        verify { stepRepository.delete(any()) }
    }

    @Test
    fun `createStep - mvpTest가 없는 경우 ModelNotFoundException`() {
        // given
        val testId = 1L
        val enterpriseId = 1L
        val request = CreateStepRequest(
            title = "test_title",
            requirement = "test_requirement",
            guidelineUrl = "test_url",
            reward = 100,
        )

        every { mvpTestRepository.findByIdOrNull(testId) } returns null

        //then
        shouldThrow<ModelNotFoundException> {
            stepService.createStep(enterpriseId, testId, request)
        }
    }

    @Test
    fun `createStep - enterpriseId가 다른 경우 NoPermissoinException 발생`() {
        val testId = 1L
        val enterpriseId = 1L
        val request = CreateStepRequest(
            title = "title",
            requirement = "requirement",
            guidelineUrl = "url",
            reward = 100,
        )

        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 2L,
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
            requirementSex = true,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        )

        val maxOrder = 0

        val step = Step(
            id = 1L,
            title = request.title,
            requirement = request.requirement,
            guidelineUrl = request.guidelineUrl,
            reward = request.reward,
            stepOrder = maxOrder + 1,
            mvpTest = mvpTest
        )

        every { mvpTestRepository.findByIdOrNull(testId) } returns mvpTest
        every { stepRepository.save(any()) } returns step

        //then
        shouldThrow<NoPermissionException> {
            stepService.createStep(enterpriseId, testId, request)
        }
    }

    @Test
    fun `updateStepById - enterpriseId가 다른 경우 NoPermissionException`() {
        //given
        val stepId = 1L
        val testId = 1L
        val enterpriseId = 1L
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 2L,
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
            requirementSex = true,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        )

        val step = Step(
            id = 1L,
            title = "title",
            requirement = "requirement",
            guidelineUrl = "string",
            reward = 100,
            stepOrder = 1,
            mvpTest = mvpTest
        )

        val request = UpdateStepRequest(
            title = "test_title",
            requirement = "test_requirement",
            guidelineUrl = "test_url",
        )
        every { mvpTestRepository.findByIdOrNull(testId) } returns mvpTest
        every { stepRepository.findByIdOrNull(stepId) } returns step

        //then
        shouldThrow<NoPermissionException> {
            stepService.updateStepById(enterpriseId, stepId, request)
        }

    }

    @Test
    fun `deleteStepById - enterpriseId가 다른 경우 NoPermissionException`() {
        // given
        val stepId = 1L
        val testId = 1L
        val enterpriseId = 1L
        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 2L,
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
            requirementSex = true,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        )

        val step = Step(
            id = 1L,
            title = "title",
            requirement = "requirement",
            guidelineUrl = "string",
            reward = 100,
            stepOrder = 1,
            mvpTest = mvpTest
        )

        every { mvpTestRepository.findByIdOrNull(testId) } returns mvpTest
        every { stepRepository.findByIdOrNull(stepId) } returns step
        every { stepRepository.delete(any()) } just Runs

        //then
        shouldThrow<NoPermissionException> {
            stepService.deleteStepById(enterpriseId, stepId)
        }
    }

    @Test
    fun `updateStepById - step이 존재하지 않는 경우 ModelNotFoundException`() {
        // given
        val stepId = 1L
        val testId = 1L
        val enterpriseId = 1L

        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
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
            requirementSex = true,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        )


        val request = UpdateStepRequest(
            title = "test_title",
            requirement = "test_requirement",
            guidelineUrl = "test_url"
        )

        every { mvpTestRepository.findByIdOrNull(testId) } returns mvpTest
        every { stepRepository.findByIdOrNull(stepId) } returns null

        // then
        shouldThrow<ModelNotFoundException> {
            stepService.updateStepById(enterpriseId, stepId, request)
        }
    }

    @Test
    fun `deleteStepById - step이 존재하지 않는 경우 ModelNotFoundException`() {
        // given
        val stepId = 1L
        val testId = 1L
        val enterpriseId = 1L

        val mvpTest = MvpTest(
            id = 1L,
            enterpriseId = 1L,
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
            requirementSex = true,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 50,
            state = MvpTestState.APPROVED
        )

        every { mvpTestRepository.findByIdOrNull(testId) } returns mvpTest
        every { stepRepository.findByIdOrNull(stepId) } returns null

        shouldThrow<ModelNotFoundException> {
            stepService.deleteStepById(enterpriseId, stepId)
        }
    }

}






