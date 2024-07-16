package com.team1.mvp_test.domain.mvptest.service

import com.team1.mvp_test.domain.mvptest.repository.MvpTestRepository
import org.springframework.stereotype.Service

@Service
class MvpTestService(
    private val mvpTestRepository: MvpTestRepository
) {
    fun createMvpTest(request: CreateMpvTestRequest): MvpTestResponse? {
    }
}