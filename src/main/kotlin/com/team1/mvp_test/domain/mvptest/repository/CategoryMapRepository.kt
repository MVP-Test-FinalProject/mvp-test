package com.team1.mvp_test.domain.mvptest.repository

import com.team1.mvp_test.domain.mvptest.model.CategoryMap
import com.team1.mvp_test.domain.mvptest.model.CategoryMapId
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryMapRepository: JpaRepository<CategoryMap, CategoryMapId> {
}