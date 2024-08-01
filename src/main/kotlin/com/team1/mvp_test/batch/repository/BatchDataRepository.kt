package com.team1.mvp_test.batch.repository

import com.team1.mvp_test.batch.model.BatchData
import org.springframework.data.jpa.repository.JpaRepository

interface BatchDataRepository : JpaRepository<BatchData, Long> {

}