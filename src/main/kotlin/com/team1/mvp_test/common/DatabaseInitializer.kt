package com.team1.mvp_test.common

import com.team1.mvp_test.domain.category.model.Category
import com.team1.mvp_test.domain.category.repository.CategoryRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(
    private val categoryRepository: CategoryRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        categoryRepository.save(Category(name = "웹사이트"))
        categoryRepository.save(Category(name = "안드로이드"))
        categoryRepository.save(Category(name = "IOS"))
    }
}