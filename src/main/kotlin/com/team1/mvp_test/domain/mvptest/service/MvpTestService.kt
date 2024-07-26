package com.team1.mvp_test.domain.mvptest.service

import com.team1.mvp_test.common.error.CategoryErrorMessage
import com.team1.mvp_test.common.error.MvpTestErrorMessage
import com.team1.mvp_test.common.exception.ModelNotFoundException
import com.team1.mvp_test.common.exception.NoPermissionException
import com.team1.mvp_test.domain.category.repository.CategoryRepository
import com.team1.mvp_test.domain.member.model.MemberTest
import com.team1.mvp_test.domain.member.model.MemberTestState
import com.team1.mvp_test.domain.member.repository.MemberRepository
import com.team1.mvp_test.domain.member.repository.MemberTestRepository
import com.team1.mvp_test.domain.member.service.MemberService
import com.team1.mvp_test.domain.mvptest.dto.CreateMvpTestRequest
import com.team1.mvp_test.domain.mvptest.dto.MvpTestResponse
import com.team1.mvp_test.domain.mvptest.dto.UpdateMvpTestRequest
import com.team1.mvp_test.domain.mvptest.model.MvpTestCategoryMap
import com.team1.mvp_test.domain.mvptest.model.RecruitType
import com.team1.mvp_test.domain.mvptest.repository.MvpTestCategoryMapRepository
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class MvpTestService(
    private val mvpTestRepository: MvpTestRepository,
    private val categoryRepository: CategoryRepository,
    private val mvpTestCategoryMapRepository: MvpTestCategoryMapRepository,
    private val memberRepository: MemberRepository,
    private val memberTestRepository: MemberTestRepository,
    private val memberService: MemberService
) {
    @Transactional
    fun createMvpTest(enterpriseId: Long, request: CreateMvpTestRequest): MvpTestResponse {
        checkRequirement(
            recruitStartDate = request.recruitStartDate,
            recruitEndDate = request.recruitEndDate,
            testStartDate = request.testStartDate,
            testEndDate = request.testEndDate,
            minAge = request.requirementMinAge,
            maxAge = request.requirementMaxAge
        )
        val mvpTest = request.toMvpTest(enterpriseId)
            .let { mvpTestRepository.save(it) }
        request.categories.forEach {
            val category = categoryRepository.findByName(it)
                ?: throw IllegalArgumentException(CategoryErrorMessage.NOT_EXIST.message)
            MvpTestCategoryMap(
                mvpTest = mvpTest,
                category = category
            ).let { map -> mvpTestCategoryMapRepository.save(map) }
        }
        return MvpTestResponse.from(mvpTest, request.categories)
    }

    @Transactional
    fun updateMvpTest(enterpriseId: Long, testId: Long, request: UpdateMvpTestRequest): MvpTestResponse {
        checkRequirement(
            recruitStartDate = request.recruitStartDate,
            recruitEndDate = request.recruitEndDate,
            testStartDate = request.testStartDate,
            testEndDate = request.testEndDate,
            minAge = request.requirementMinAge,
            maxAge = request.requirementMaxAge
        )
        val mvpTest = mvpTestRepository.findByIdOrNull(testId)
            ?: throw ModelNotFoundException("MvpTest", testId)
        if (mvpTest.enterpriseId != enterpriseId) throw NoPermissionException(MvpTestErrorMessage.NOT_AUTHORIZED.message)

        mvpTestCategoryMapRepository.findAllByMvpTestId(testId)
            .let { mvpTestCategoryMapRepository.deleteAll(it) }

        request.categories.forEach {
            val category = categoryRepository.findByName(it)
                ?: throw IllegalArgumentException(CategoryErrorMessage.NOT_EXIST.message)
            MvpTestCategoryMap(
                mvpTest = mvpTest,
                category = category
            ).let { map -> mvpTestCategoryMapRepository.save(map) }
        }
        mvpTest.update(request.toObject())
        return MvpTestResponse.from(mvpTest, request.categories)
    }

    @Transactional
    fun deleteMvpTest(enterpriseId: Long, testId: Long) {
        val mvpTest = mvpTestRepository.findByIdOrNull(testId)
            ?: throw ModelNotFoundException("MvpTest", testId)
        if (mvpTest.enterpriseId != enterpriseId) throw NoPermissionException(MvpTestErrorMessage.NOT_AUTHORIZED.message)
        mvpTestRepository.delete(mvpTest)
    }

    fun getMvpTest(testId: Long): MvpTestResponse {
        val mvpTest = mvpTestRepository.findByIdOrNull(testId)
            ?: throw ModelNotFoundException("MvpTest", testId)
        val categories = mvpTestCategoryMapRepository.findAllByMvpTestId(testId)
            .map { it.category.name }
        return MvpTestResponse.from(mvpTest, categories)
    }

    fun getMvpTestList(): List<MvpTestResponse> {
        return mvpTestRepository.findAll()
            .map { mvpTest ->
                mvpTestCategoryMapRepository.findAllByMvpTestId(mvpTest.id!!)
                    .map { maps -> maps.category.name }
                    .let { categories -> MvpTestResponse.from(mvpTest, categories) }
            }
    }

    @Transactional
    fun applyToMvpTest(memberId: Long, testId: Long) {
        val test = mvpTestRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException("mvpTest", testId)
        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("member", memberId)
        memberService.checkMemberActive(member)
        val recruitCount = memberTestRepository.countByTestIdAndState(testId, MemberTestState.APPROVED)
        check(recruitCount < test.recruitNum) { MvpTestErrorMessage.TEST_ALREADY_FULL.message }
        MemberTest(
            member = member,
            test = test,
            state = if (test.recruitType == RecruitType.FIRST_COME) MemberTestState.APPROVED else MemberTestState.PENDING
        ).let { memberTestRepository.save(it) }
    }

    private fun checkRequirement(
        recruitStartDate: LocalDateTime,
        recruitEndDate: LocalDateTime,
        testStartDate: LocalDateTime,
        testEndDate: LocalDateTime,
        minAge: Int?,
        maxAge: Int?
    ) {
        require(
            isRecruitDateValid(
                recruitStartDate,
                recruitEndDate
            )
        ) { MvpTestErrorMessage.RECRUIT_DATE_NOT_VALID.message }
        require(isTestDateValid(recruitEndDate, testStartDate, testEndDate)) {
            MvpTestErrorMessage.TEST_DATE_NOT_VALID.message
        }
        require(
            isAgeRuleValid(
                minAge,
                maxAge
            )
        ) { MvpTestErrorMessage.AGE_RULE_INVALID.message }
    }

    private fun isRecruitDateValid(startDate: LocalDateTime, endDate: LocalDateTime): Boolean {
        return startDate.isBefore(endDate)
    }

    private fun isTestDateValid(
        recruitEndDate: LocalDateTime,
        testStartDate: LocalDateTime,
        testEndDate: LocalDateTime
    ): Boolean {
        return testStartDate.isAfter(recruitEndDate) &&
                testEndDate.isAfter(testStartDate)
    }

    private fun isAgeRuleValid(minAge: Int?, maxAge: Int?): Boolean {
        return if (maxAge == null || minAge == null) true
        else if (minAge <= maxAge) true
        else false
    }
}