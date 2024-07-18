package com.team1.mvp_test.domain.mvptest.service

import com.team1.mvp_test.common.error.CategoryErrorMessage
import com.team1.mvp_test.common.error.EnterpriseErrorMessage
import com.team1.mvp_test.common.exception.ModelNotFoundException
import com.team1.mvp_test.domain.mvptest.dto.mvptest.CreateCategoryRequest
import com.team1.mvp_test.domain.mvptest.dto.mvptest.CreateMvpTestRequest
import com.team1.mvp_test.domain.mvptest.dto.mvptest.MvpTestListResponse
import com.team1.mvp_test.domain.mvptest.dto.mvptest.MvpTestResponse
import com.team1.mvp_test.domain.mvptest.dto.mvptest.UpdateMvpTestRequest
import com.team1.mvp_test.domain.mvptest.model.Category
import com.team1.mvp_test.domain.mvptest.model.CategoryMap
import com.team1.mvp_test.domain.mvptest.model.CategoryMapId
import com.team1.mvp_test.domain.mvptest.repository.CategoryMapRepository
import com.team1.mvp_test.domain.mvptest.repository.CategoryRepository
import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MvpTestService(
    private val mvpTestRepository: MvpTestRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryMapRepository: CategoryMapRepository
) {
    @Transactional
    fun createMvpTest(enterpriseId: Long, request: CreateMvpTestRequest): MvpTestResponse {
        val mvpTest = request.toMvpTest(enterpriseId)
        mvpTest.state = ""
        mvpTest.rejectReason = ""
        val savedMvpTest = mvpTestRepository.save(mvpTest)
        val categoryMaps = request.category.map {
            val category = categoryRepository.findByName(it)
                ?: throw IllegalArgumentException(CategoryErrorMessage.NOT_EXIST.message)
            categoryMapRepository.save(CategoryMap(id = CategoryMapId(category.id, mvpTest.id), category))
        }

        savedMvpTest.categories = categoryMaps
        mvpTestRepository.save(savedMvpTest)
        return MvpTestResponse.from(savedMvpTest)
    }

    @Transactional
    fun updateMvpTest(id: Long, testId: Long, request: UpdateMvpTestRequest): MvpTestResponse {
        val mvpTest = mvpTestRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException(
            "MvpTest",
            testId
        )
        checkMvpTestAuthor(id, mvpTest.enterpriseId)
        val categoryMaps = request.category.map {
            val category = categoryRepository.findByName(it)
                ?: throw IllegalArgumentException(CategoryErrorMessage.NOT_EXIST.message)
            CategoryMap(id = CategoryMapId(category.id, mvpTest.id), category)
        }
        mvpTest.update(request, categoryMaps)
        return MvpTestResponse.from(mvpTestRepository.save(mvpTest))
    }

    fun deleteMvpTest(id: Long, testId: Long) {
        val mvpTest = mvpTestRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException(
            "MvpTest",
            testId
        )
        checkMvpTestAuthor(id, mvpTest.enterpriseId)
        mvpTestRepository.deleteById(testId)
    }

    fun getMvpTest(testId: Long): MvpTestResponse? {
        val mvpTest = mvpTestRepository.findByIdOrNull(testId) ?: throw ModelNotFoundException(
            "MvpTest",
            testId
        )
        return MvpTestResponse.from(mvpTest)
    }

    fun getMvpTestList(): MvpTestListResponse? {
        return MvpTestListResponse(mvpTestRepository.findAll().map {
            MvpTestResponse.from(it)
        }
        )
    }

    private fun checkMvpTestAuthor(enterpriseId: Long, MvpTestAuthorId: Long) {
        check(enterpriseId == MvpTestAuthorId) { EnterpriseErrorMessage.NOT_AUTHORIZED.message }
    }

    fun createCategory(request: CreateCategoryRequest): String {
        return categoryRepository.save(Category(name = request.category)).name
    }
}