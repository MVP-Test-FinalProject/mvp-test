package com.team1.mvp_test.domain.mvptest.repository

import com.team1.mvp_test.domain.mvptest.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<Category, Long> {
    fun findByName(name: String): Category?
}