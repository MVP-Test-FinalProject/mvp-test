package com.team1.mvp_test.domain.step.service

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
import com.team1.mvp_test.infra.s3.s3service.S3Service
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import java.time.LocalDateTime


class StepServiceTest : BehaviorSpec({

    val stepRepository = mockk<StepRepository>()
    val mvpTestRepository = mockk<MvpTestRepository>()
    val s3Service = mockk<S3Service>()
    val stepService = StepService(
        stepRepository = stepRepository,
        mvpTestRepository = mvpTestRepository,
        s3Service = s3Service
    )
    Given("createStep 실행 시"){
        every { mvpTestRepository.findByIdOrNull(TEST_ID) } returns mvpTest
        every { s3Service.uploadStepFile(invalidFile) } throws IllegalArgumentException("Invalid file type. Only ppt and pdf are allowed")
        every { stepRepository.findMaxOrderByTestId(TEST_ID) } returns 1
        When("파일 형식이 ppt,pdf가 아니면"){
            Then("IllegalArgumentException 예외 발생"){
                shouldThrow<IllegalArgumentException>{
                    stepService.createStep(ENTERPRISE_ID, TEST_ID, createStepRequest, invalidFile)
                }
            }
        }
    }

    Given("updateStep 실행 시") {
        every { s3Service.uploadStepFile(invalidFile) } throws IllegalArgumentException("Invalid file type. Only ppt and pdf are allowed")
        every { stepRepository.findByIdOrNull(STEP_ID) } returns step
        every { stepRepository.findMaxOrderByTestId(TEST_ID) } returns MAX_ORDER
        every { s3Service.deleteFile(any()) } returns Unit
        When("파일 형식이 ppt, pdf가 아니면") {
            Then("IllegalArgumentException 예외 발생") {
                shouldThrow<IllegalArgumentException> {
                    stepService.updateStep(ENTERPRISE_ID, STEP_ID, updateStepRequest, invalidFile)
                }

            }
        }
    }

    Given("존재하지 않는 테스트에 대해서") {
        every { mvpTestRepository.findByIdOrNull(any()) } returns null
        When("createStep 실행 시") {
            Then("ModelNotFoundException 예외를 던진다") {
                shouldThrow<ModelNotFoundException> {
                    stepService.createStep(ENTERPRISE_ID, TEST_ID, createStepRequest, guidelineFile)
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
                    stepService.createStep(otherEnterpriseId, TEST_ID, createStepRequest, guidelineFile)
                }
            }
        }
        When("updateStep 실행 시") {
            Then("NoPermissionException 예외를 던진다") {
                shouldThrow<NoPermissionException> {
                    stepService.updateStep(otherEnterpriseId, STEP_ID, updateStepRequest, guidelineFile)
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
                    stepService.updateStep(ENTERPRISE_ID, STEP_ID, updateStepRequest, guidelineFile)
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
            reward = 100
        )
        private val updateStepRequest = UpdateStepRequest(
            title = "test_title",
            requirement = "test_requirement",
        )
        private const val MAX_ORDER = 0

        private val guidelineFile = MockMultipartFile(
            "file2",
            "test2.jpg",
            "image/jpeg",
            ByteArray(1)
        )
        private val step = Step(
            id = STEP_ID,
            title = createStepRequest.title,
            requirement = createStepRequest.requirement,
            guidelineUrl = "test.ppt",
            reward = createStepRequest.reward,
            stepOrder = MAX_ORDER + 1,
            mvpTest = mvpTest
        )
        private val invalidFile = MockMultipartFile(
            "invalidFile",
            "empty.jpg",
            "jpeg",
            ByteArray(1)
        )

    }
}






