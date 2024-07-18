package com.team1.mvp_test.domain.mvptest.repository

import com.team1.mvp_test.domain.mvptest.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, Long> {
    fun findByName(name: String): Category
}