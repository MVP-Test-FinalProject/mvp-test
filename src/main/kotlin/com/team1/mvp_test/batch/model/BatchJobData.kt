package com.team1.mvp_test.batch.model

import jakarta.persistence.*

@Entity
@Table(name = "batch_job_data")
class BatchJobData(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: BatchStatus,

    @Column(name = "test_id")
    var testId: Long,

    @ManyToOne
    @JoinColumn(name = "batch_id")
    val batch: BatchData
)