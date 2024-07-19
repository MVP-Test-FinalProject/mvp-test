package com.team1.mvp_test.domain.mvptest.repository

import com.team1.mvp_test.domain.mvptest.model.MvpTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MvpTestRepository : JpaRepository<MvpTest, Long>{
}