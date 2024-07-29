package com.team1.mvp_test.domain.mvptest.service

import com.team1.mvp_test.domain.category.repository.CategoryRepository
import com.team1.mvp_test.domain.member.model.Sex
import com.team1.mvp_test.domain.member.repository.MemberRepository
import com.team1.mvp_test.domain.member.repository.MemberTestRepository
import com.team1.mvp_test.domain.member.service.MemberService
import com.team1.mvp_test.domain.mvptest.dto.CreateMvpTestRequest
import com.team1.mvp_test.domain.mvptest.dto.UpdateMvpTestRequest
import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.mvptest.model.MvpTestState
import com.team1.mvp_test.domain.mvptest.model.RecruitType
import com.team1.mvp_test.domain.mvptest.repository.MvpTestCategoryMapRepository
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import com.team1.mvp_test.infra.redisson.RedissonService
import com.team1.mvp_test.infra.s3.s3service.S3Service
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import java.time.LocalDateTime

class MvpTestServiceTest : BehaviorSpec({

    val mvpTestRepository = mockk<MvpTestRepository>()
    val categoryRepository = mockk<CategoryRepository>()
    val mvpTestCategoryMapRepository = mockk<MvpTestCategoryMapRepository>()
    val s3Service = mockk<S3Service>()
    val memberRepository = mockk<MemberRepository>()
    val memberTestRepository = mockk<MemberTestRepository>()
    val memberService = mockk<MemberService>()
    val redissonService = mockk<RedissonService>()

    val mvpTestService = MvpTestService(
        mvpTestRepository = mvpTestRepository,
        categoryRepository = categoryRepository,
        mvpTestCategoryMapRepository = mvpTestCategoryMapRepository,
        memberRepository = memberRepository,
        memberTestRepository = memberTestRepository,
        s3Service = s3Service,
        redissonService = redissonService,
        memberService = memberService
    )

    Given("createMvpTest 실행 ") {
        every { s3Service.uploadMvpTestFile(invalidFile) } throws IllegalArgumentException("Invalid file type. Only JPEG and PNG are allowed.")
        When("파일 형식이 jpg, jpeg, png 이 아니면") {
            Then("IllegalArgumentException 예외 발생") {
                shouldThrow<IllegalArgumentException> {
                    mvpTestService.createMvpTest(ENTERPRISE_ID, createMvpTestRequest, invalidFile)
                }
            }
        }
    }
    Given("createMvpTest 실행 시 ") {
        every { s3Service.uploadMvpTestFile(emptyFile) } throws IllegalArgumentException("Invalid file type. Only JPEG and PNG are allowed.")
        When("파일이 없으면 ") {
            Then("IllegalArgumentException 예외 발생") {
                shouldThrow<IllegalArgumentException> {
                    mvpTestService.createMvpTest(ENTERPRISE_ID, createMvpTestRequest, emptyFile)
                }
            }
        }
    }
    Given("updateMvpTest 실행 시") {
        every { s3Service.uploadMvpTestFile(invalidFile) } throws IllegalArgumentException("Invalid file type. Only JPEG and PNG are allowed.")
        every { mvpTestRepository.findByIdOrNull(TEST_ID) } returns mvpTest
        every { s3Service.deleteFile(any()) } returns Unit
        every { mvpTestCategoryMapRepository.findAllByMvpTestId(TEST_ID) } returns emptyList()
        every { mvpTestCategoryMapRepository.deleteAll(any()) } returns Unit
        When("파일 형식이 jpg, jpeg, png 이 아니면") {
            Then("IllegalArgumentException 예외 발생") {
                shouldThrow<IllegalArgumentException> {
                    mvpTestService.updateMvpTest(ENTERPRISE_ID, TEST_ID, updateMvpTestRequest, invalidFile)
                }
            }
        }
    }
    Given("updateMvpTest 실행") {
        every { s3Service.uploadMvpTestFile(emptyFile) } throws IllegalArgumentException("Invalid file type. Only JPEG and PNG are allowed.")
        every { mvpTestRepository.findByIdOrNull(TEST_ID) } returns mvpTest
        every { s3Service.deleteFile(any()) } returns Unit
        every { mvpTestCategoryMapRepository.findAllByMvpTestId(TEST_ID) } returns emptyList()
        every { mvpTestCategoryMapRepository.deleteAll(any()) } returns Unit
        When("파일이 없으면") {
            Then("IllegalArgumentException 예외 발생") {
                shouldThrow<IllegalArgumentException> {
                    mvpTestService.updateMvpTest(ENTERPRISE_ID, TEST_ID, updateMvpTestRequest, emptyFile)
                }
            }
        }
    }

}) {
    companion object {
        private const val TEST_ID = 1L
        private const val ENTERPRISE_ID = 1L
        private val mvpTest = MvpTest(
            id = TEST_ID,
            enterpriseId = ENTERPRISE_ID,
            mvpName = "string",
            recruitStartDate = LocalDateTime.of(2025, 5, 1, 12, 0),
            recruitEndDate = LocalDateTime.of(2025, 5, 5, 12, 0),
            testStartDate = LocalDateTime.of(2025, 5, 10, 12, 0),
            testEndDate = LocalDateTime.of(2025, 5, 15, 12, 0),
            mainImageUrl = "string.jpg",
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

        private val createMvpTestRequest = CreateMvpTestRequest(
            mvpName = "Valid MVP",
            recruitStartDate = LocalDateTime.of(2025, 7, 24, 0, 0),
            recruitEndDate = LocalDateTime.of(2025, 7, 26, 0, 0),
            testStartDate = LocalDateTime.of(2025, 8, 1, 0, 0),
            testEndDate = LocalDateTime.of(2025, 8, 2, 0, 0),
            mvpInfo = "Valid info",
            mvpUrl = "https://mvp.casd",
            rewardBudget = 10000,
            requirementMinAge = 15,
            requirementMaxAge = 20,
            requirementSex = Sex.FEMALE,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 10,
            categories = listOf("ValidCategory")
        )

        private val updateMvpTestRequest = UpdateMvpTestRequest(
            mvpName = "Valid MVP Name",
            recruitStartDate = LocalDateTime.of(2025, 9, 1, 12, 0),
            recruitEndDate = LocalDateTime.of(2025, 9, 5, 12, 0),
            testStartDate = LocalDateTime.of(2025, 9, 10, 12, 0),
            testEndDate = LocalDateTime.of(2025, 9, 15, 12, 0),
            mvpInfo = "Valid info",
            mvpUrl = "https://mvp.casd",
            rewardBudget = 10000,
            requirementMinAge = 15,
            requirementMaxAge = 20,
            requirementSex = Sex.FEMALE,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = 10,
            categories = listOf()
        )

        private val emptyFile = MockMultipartFile(
            "emptyFile",
            "empty.jpg",
            "jpg",
            ByteArray(0)
        )

        private val invalidFile = MockMultipartFile(
            "invalidFile",
            "test.pdf",
            "pdf",
            ByteArray(1)
        )
    }

}
