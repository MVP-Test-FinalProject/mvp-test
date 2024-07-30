package com.team1.mvp_test.batch.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "batch_data")
class BatchData(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: BatchStatus = BatchStatus.FAILED,

    @Column(name = "total_count")
    var totalCount: Int = 0,

    @Column(name = "failed_count")
    var failedCount: Int = 0,

    @Column(name = "start_time")
    var startTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "end_time")
    var endTime: LocalDateTime = LocalDateTime.now(),


    )