package com.team1.mvp_test.batch.repository

import com.team1.mvp_test.batch.model.BatchJobData
import org.springframework.data.jpa.repository.JpaRepository

interface BatchJobDataRepository : JpaRepository<BatchJobData, Long> {
    fun findAllByBatchId(batchId: Long): List<BatchJobData>
}