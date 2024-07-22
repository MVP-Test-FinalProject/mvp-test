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

@Service
class MvpTestService(
    private val mvpTestRepository: MvpTestRepository,
    private val categoryRepository: CategoryRepository,
    private val mvpTestCategoryMapRepository: MvpTestCategoryMapRepository,
    private val memberRepository: MemberRepository,
    private val memberTestRepository: MemberTestRepository
) {
    @Transactional
    fun createMvpTest(enterpriseId: Long, request: CreateMvpTestRequest): MvpTestResponse {
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
        mvpTest.update(request)
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
        val recruitCount = memberTestRepository.countByTestIdAndState(testId, MemberTestState.APPROVED)
        check(recruitCount >= test.recruitNum) { MvpTestErrorMessage.TEST_ALREADY_FULL.message }
        MemberTest(
            member = member,
            test = test,
            state = if (test.recruitType == RecruitType.FIRST_COME) MemberTestState.APPROVED else MemberTestState.PENDING
        ).let { memberTestRepository.save(it) }
    }
//
//    @Transactional
//    fun approveMemberToTest(testId: Long, memberId: Long, enterpriseId: Long): TestingMemberCountResponse {
//        val test = mvpTestRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException("mvpTest", testId)
//        checkMvpTestAuthor(enterpriseId, test.enterpriseId)
//        val member = memberRepository.findByIdOrNull(memberId) ?: throw ModelNotFoundException("member", memberId)
//        val currentTestingMembersCount = memberTestRepository.countAllTestingMembers(testId)
//        if (currentTestingMembersCount >= test.recruitNum) {
//            throw IllegalArgumentException(MvpTestErrorMessage.TEST_ALREADY_FULL.message)
//        }
//        memberTestRepository.save(MemberTest(member = member, test = test))
//        return TestingMemberCountResponse.from(test, currentTestingMembersCount + 1)
//    }
//
//    @Transactional
//    fun undoApproveMemberToTest(testId: Long, memberId: Long, enterpriseId: Long): TestingMemberCountResponse {
//        val test = mvpTestRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException("mvpTest", testId)
//        checkMvpTestAuthor(enterpriseId, test.enterpriseId)
//        val memberTest = memberTestRepository.findByMemberIdAndTestId(memberId, testId) ?: throw ModelNotFoundException(
//            MvpTestErrorMessage.NOT_TEST_MEMBER.message
//        )
//        val currentTestingMembersCount = memberTestRepository.countAllTestingMembers(testId)
//        return TestingMemberCountResponse.from(test, currentTestingMembersCount - 1)
//    }
//
}