package com.team1.mvp_test.domain.mvptest.model

import com.team1.mvp_test.domain.member.model.Sex
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDateTime

@DataJpaTest
class MvpTestTest {
    companion object {
        private lateinit var validator: Validator

        @BeforeAll
        @JvmStatic
        fun setup() {
            val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
            validator = factory.validator
        }
    }

    @Test
    fun validateNameTest() {
        // Arrange
        val enterpriseId = 1L
        val mvpName = "testName"
        val recruitStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 18, 11, 22)
        val recruitEndDate: LocalDateTime = LocalDateTime.of(2025, 1, 18, 11, 22)
        val testStartDate: LocalDateTime = LocalDateTime.of(2024, 3, 18, 11, 22)
        val testEndDate: LocalDateTime = LocalDateTime.of(2025, 3, 18, 11, 22)
        val mainImageUrl = "imageUrl"
        val mvpInfo = "this is mvpTest for test"
        val mvpUrl = "this is test Url"
        val rewardBudget = 10000
        val requirementMinAge = 20
        val requirementMaxAge = 40
        val recruitNum = 50

        // Act
        val actual = MvpTest(
            enterpriseId = enterpriseId,
            mvpName = mvpName,
            recruitStartDate = recruitStartDate,
            recruitEndDate = recruitEndDate,
            testStartDate = testStartDate,
            testEndDate = testEndDate,
            mainImageUrl = mainImageUrl,
            mvpInfo = mvpInfo,
            mvpUrl = mvpUrl,
            rewardBudget = rewardBudget,
            requirementMinAge = requirementMinAge,
            requirementMaxAge = requirementMaxAge,
            requirementSex = Sex.MALE,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = recruitNum,
            state = MvpTestState.APPROVED
        )

        // Assert
        assertThat(actual).isNotNull
        assertThat(actual.mvpName).isEqualTo(mvpName)
    }

    @Test
    fun validateWrongNameTest() {
        // Arrange
        val enterpriseId = 1L
        val mvpName = "testName".repeat(10)
        val recruitStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 18, 11, 22)
        val recruitEndDate: LocalDateTime = LocalDateTime.of(2025, 1, 18, 11, 22)
        val testStartDate: LocalDateTime = LocalDateTime.of(2024, 3, 18, 11, 22)
        val testEndDate: LocalDateTime = LocalDateTime.of(2025, 3, 18, 11, 22)
        val mainImageUrl = "imageUrl"
        val mvpInfo = "this is mvpTest for test"
        val mvpUrl = "this is test Url"
        val rewardBudget = 10000
        val requirementMinAge = 20
        val requirementMaxAge = 40
        val recruitNum = 50

        // Act
        val actual = MvpTest(
            enterpriseId = enterpriseId,
            mvpName = mvpName,
            recruitStartDate = recruitStartDate,
            recruitEndDate = recruitEndDate,
            testStartDate = testStartDate,
            testEndDate = testEndDate,
            mainImageUrl = mainImageUrl,
            mvpInfo = mvpInfo,
            mvpUrl = mvpUrl,
            rewardBudget = rewardBudget,
            requirementMinAge = requirementMinAge,
            requirementMaxAge = requirementMaxAge,
            requirementSex = Sex.MALE,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = recruitNum,
            state = MvpTestState.APPROVED
        )

        // Assert
        val constraintViolations = validator.validate(actual)
        assertThat(constraintViolations).isNotEmpty
        assertThat(constraintViolations).hasSize(1)
        assertThat(constraintViolations.first().message).isEqualTo("크기가 1에서 50 사이여야 합니다")
    }

    @Test
    fun validateWrongRecruitEndDateTest() {
        // Arrange
        val enterpriseId = 1L
        val mvpName = "testName"
        val recruitStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 18, 11, 22)
        val recruitEndDate: LocalDateTime = LocalDateTime.of(2024, 1, 11, 11, 22)
        val testStartDate: LocalDateTime = LocalDateTime.of(2024, 3, 18, 11, 22)
        val testEndDate: LocalDateTime = LocalDateTime.of(2025, 3, 18, 11, 22)
        val mainImageUrl = "imageUrl"
        val mvpInfo = "this is mvpTest for test"
        val mvpUrl = "this is test Url"
        val rewardBudget = 10000
        val requirementMinAge = 20
        val requirementMaxAge = 40
        val recruitNum = 50

        // Act
        val actual = assertThrows<IllegalArgumentException> {
            MvpTest(
                enterpriseId = enterpriseId,
                mvpName = mvpName,
                recruitStartDate = recruitStartDate,
                recruitEndDate = recruitEndDate,
                testStartDate = testStartDate,
                testEndDate = testEndDate,
                mainImageUrl = mainImageUrl,
                mvpInfo = mvpInfo,
                mvpUrl = mvpUrl,
                rewardBudget = rewardBudget,
                requirementMinAge = requirementMinAge,
                requirementMaxAge = requirementMaxAge,
                requirementSex = Sex.MALE,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = recruitNum,
                state = MvpTestState.APPROVED
            )
        }

        // Assert
        assertThat(actual.message).isEqualTo("모집 일자가 유효하지 않습니다.")
    }

    @Test
    fun validateWrongTestStartDateTest() {
        // Arrange
        val enterpriseId = 1L
        val mvpName = "testName"
        val recruitStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 18, 11, 22)
        val recruitEndDate: LocalDateTime = LocalDateTime.of(2025, 1, 11, 11, 22)
        val testStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 11, 11, 22)
        val testEndDate: LocalDateTime = LocalDateTime.of(2025, 3, 18, 11, 22)
        val mainImageUrl = "imageUrl"
        val mvpInfo = "this is mvpTest for test"
        val mvpUrl = "this is test Url"
        val rewardBudget = 10000
        val requirementMinAge = 20
        val requirementMaxAge = 40
        val recruitNum = 50

        // Act
        val actual = assertThrows<IllegalArgumentException> {
            MvpTest(
                enterpriseId = enterpriseId,
                mvpName = mvpName,
                recruitStartDate = recruitStartDate,
                recruitEndDate = recruitEndDate,
                testStartDate = testStartDate,
                testEndDate = testEndDate,
                mainImageUrl = mainImageUrl,
                mvpInfo = mvpInfo,
                mvpUrl = mvpUrl,
                rewardBudget = rewardBudget,
                requirementMinAge = requirementMinAge,
                requirementMaxAge = requirementMaxAge,
                requirementSex = Sex.MALE,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = recruitNum,
                state = MvpTestState.APPROVED
            )
        }

        // Assert
        assertThat(actual.message).isEqualTo("테스트 일자가 유효하지 않습니다.")
    }

    @Test
    fun validateWrongTestEndDateTest() {
        // Arrange
        val enterpriseId = 1L
        val mvpName = "testName"
        val recruitStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 18, 11, 22)
        val recruitEndDate: LocalDateTime = LocalDateTime.of(2025, 1, 11, 11, 22)
        val testStartDate: LocalDateTime = LocalDateTime.of(2024, 3, 18, 11, 22)
        val testEndDate: LocalDateTime = LocalDateTime.of(2024, 2, 18, 11, 22)
        val mainImageUrl = "imageUrl"
        val mvpInfo = "this is mvpTest for test"
        val mvpUrl = "this is test Url"
        val rewardBudget = 10000
        val requirementMinAge = 20
        val requirementMaxAge = 40
        val recruitNum = 50

        // Act
        val actual = assertThrows<IllegalArgumentException> {
            MvpTest(
                enterpriseId = enterpriseId,
                mvpName = mvpName,
                recruitStartDate = recruitStartDate,
                recruitEndDate = recruitEndDate,
                testStartDate = testStartDate,
                testEndDate = testEndDate,
                mainImageUrl = mainImageUrl,
                mvpInfo = mvpInfo,
                mvpUrl = mvpUrl,
                rewardBudget = rewardBudget,
                requirementMinAge = requirementMinAge,
                requirementMaxAge = requirementMaxAge,
                requirementSex = Sex.MALE,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = recruitNum,
                state = MvpTestState.APPROVED
            )
        }

        // Assert
        assertThat(actual.message).isEqualTo("테스트 일자가 유효하지 않습니다.")
    }

    @Test
    fun validateWrongRewardBudgetTest() {
        // Arrange
        val enterpriseId = 1L
        val mvpName = "testName"
        val recruitStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 18, 11, 22)
        val recruitEndDate: LocalDateTime = LocalDateTime.of(2025, 1, 18, 11, 22)
        val testStartDate: LocalDateTime = LocalDateTime.of(2024, 3, 18, 11, 22)
        val testEndDate: LocalDateTime = LocalDateTime.of(2025, 3, 18, 11, 22)
        val mainImageUrl = "imageUrl"
        val mvpInfo = "this is mvpTest for test"
        val mvpUrl = "this is test Url"
        val rewardBudget = 1000
        val requirementMinAge = 20
        val requirementMaxAge = 40
        val recruitNum = 50

        // Act
        val actual = MvpTest(
            enterpriseId = enterpriseId,
            mvpName = mvpName,
            recruitStartDate = recruitStartDate,
            recruitEndDate = recruitEndDate,
            testStartDate = testStartDate,
            testEndDate = testEndDate,
            mainImageUrl = mainImageUrl,
            mvpInfo = mvpInfo,
            mvpUrl = mvpUrl,
            rewardBudget = rewardBudget,
            requirementMinAge = requirementMinAge,
            requirementMaxAge = requirementMaxAge,
            requirementSex = Sex.MALE,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = recruitNum,
            state = MvpTestState.APPROVED
        )

        // Assert
        val constraintViolations = validator.validate(actual)
        assertThat(constraintViolations).isNotEmpty
        assertThat(constraintViolations).hasSize(1)
        assertThat(constraintViolations.first().message).isEqualTo("10000 이상이어야 합니다")
    }

    @Test
    fun validateWrongMinimumAgeTest() {
        // Arrange
        val enterpriseId = 1L
        val mvpName = "testName"
        val recruitStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 18, 11, 22)
        val recruitEndDate: LocalDateTime = LocalDateTime.of(2025, 1, 18, 11, 22)
        val testStartDate: LocalDateTime = LocalDateTime.of(2024, 3, 18, 11, 22)
        val testEndDate: LocalDateTime = LocalDateTime.of(2025, 3, 18, 11, 22)
        val mainImageUrl = "imageUrl"
        val mvpInfo = "this is mvpTest for test"
        val mvpUrl = "this is test Url"
        val rewardBudget = 10000
        val requirementMinAge = -1
        val requirementMaxAge = 40
        val recruitNum = 50

        // Act
        val actual = MvpTest(
            enterpriseId = enterpriseId,
            mvpName = mvpName,
            recruitStartDate = recruitStartDate,
            recruitEndDate = recruitEndDate,
            testStartDate = testStartDate,
            testEndDate = testEndDate,
            mainImageUrl = mainImageUrl,
            mvpInfo = mvpInfo,
            mvpUrl = mvpUrl,
            rewardBudget = rewardBudget,
            requirementMinAge = requirementMinAge,
            requirementMaxAge = requirementMaxAge,
            requirementSex = Sex.MALE,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = recruitNum,
            state = MvpTestState.APPROVED
        )

        // Assert
        val constraintViolations = validator.validate(actual)
        assertThat(constraintViolations).isNotEmpty
        assertThat(constraintViolations).hasSize(1)
        assertThat(constraintViolations.first().message).isEqualTo("1 이상이어야 합니다")
    }

    @Test
    fun validateWrongMaximumAgeTest() {
        // Arrange
        val enterpriseId = 1L
        val mvpName = "testName"
        val recruitStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 18, 11, 22)
        val recruitEndDate: LocalDateTime = LocalDateTime.of(2025, 1, 18, 11, 22)
        val testStartDate: LocalDateTime = LocalDateTime.of(2024, 3, 18, 11, 22)
        val testEndDate: LocalDateTime = LocalDateTime.of(2025, 3, 18, 11, 22)
        val mainImageUrl = "imageUrl"
        val mvpInfo = "this is mvpTest for test"
        val mvpUrl = "this is test Url"
        val rewardBudget = 10000
        val requirementMinAge = 60
        val requirementMaxAge = 40
        val recruitNum = 50

        // Act
        val actual = assertThrows<IllegalArgumentException> {
            MvpTest(
                enterpriseId = enterpriseId,
                mvpName = mvpName,
                recruitStartDate = recruitStartDate,
                recruitEndDate = recruitEndDate,
                testStartDate = testStartDate,
                testEndDate = testEndDate,
                mainImageUrl = mainImageUrl,
                mvpInfo = mvpInfo,
                mvpUrl = mvpUrl,
                rewardBudget = rewardBudget,
                requirementMinAge = requirementMinAge,
                requirementMaxAge = requirementMaxAge,
                requirementSex = Sex.MALE,
                recruitType = RecruitType.FIRST_COME,
                recruitNum = recruitNum,
                state = MvpTestState.APPROVED
            )
        }

        // Assert
        assertThat(actual.message).isEqualTo("최대 나이는 최소 나이보다 큰 값이어야 합니다.")
    }

    @Test
    fun validateWrongRecruitNumberTest() {
        // Arrange
        val enterpriseId = 1L
        val mvpName = "testName"
        val recruitStartDate: LocalDateTime = LocalDateTime.of(2024, 1, 18, 11, 22)
        val recruitEndDate: LocalDateTime = LocalDateTime.of(2025, 1, 18, 11, 22)
        val testStartDate: LocalDateTime = LocalDateTime.of(2024, 3, 18, 11, 22)
        val testEndDate: LocalDateTime = LocalDateTime.of(2025, 3, 18, 11, 22)
        val mainImageUrl = "imageUrl"
        val mvpInfo = "this is mvpTest for test"
        val mvpUrl = "this is test Url"
        val rewardBudget = 10000
        val requirementMinAge = 10
        val requirementMaxAge = 40
        val recruitNum = -1

        // Act
        val actual = MvpTest(
            enterpriseId = enterpriseId,
            mvpName = mvpName,
            recruitStartDate = recruitStartDate,
            recruitEndDate = recruitEndDate,
            testStartDate = testStartDate,
            testEndDate = testEndDate,
            mainImageUrl = mainImageUrl,
            mvpInfo = mvpInfo,
            mvpUrl = mvpUrl,
            rewardBudget = rewardBudget,
            requirementMinAge = requirementMinAge,
            requirementMaxAge = requirementMaxAge,
            requirementSex = Sex.MALE,
            recruitType = RecruitType.FIRST_COME,
            recruitNum = recruitNum,
            state = MvpTestState.APPROVED
        )

        // Assert
        val constraintViolations = validator.validate(actual)
        assertThat(constraintViolations).isNotEmpty
        assertThat(constraintViolations).hasSize(1)
        assertThat(constraintViolations.first().message).isEqualTo("1 이상이어야 합니다")
    }
}