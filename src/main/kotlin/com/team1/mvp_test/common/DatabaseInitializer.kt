package com.team1.mvp_test.common

import com.team1.mvp_test.admin.model.Admin
import com.team1.mvp_test.admin.repository.AdminRepository
import com.team1.mvp_test.domain.category.model.Category
import com.team1.mvp_test.domain.category.repository.CategoryRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(
    private val categoryRepository: CategoryRepository,
    private val adminRepository: AdminRepository,
    private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        categoryRepository.save(Category(name = "웹사이트"))
        categoryRepository.save(Category(name = "안드로이드"))
        categoryRepository.save(Category(name = "IOS"))
        adminRepository.save(Admin(account = "admin", password = passwordEncoder.encode("1q2w3e4r")))
    }
}