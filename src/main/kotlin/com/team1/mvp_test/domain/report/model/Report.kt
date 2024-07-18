package com.team1.mvp_test.domain.report.model

import com.team1.mvp_test.domain.member.model.MemberTest
import com.team1.mvp_test.domain.step.model.Step
import jakarta.persistence.*

@Entity
@Table(name = "report")
class Report(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title")
    var title: String,

    @Column(name = "body")
    var body: String,

    @Column(name = "feedback")
    var feedback: String,

    @Column(name = "is_confirmed")
    var isConfirmed: Boolean,

    @Column(name = "reason")
    var reason: String?,

    @ManyToOne
    @JoinColumn(name = "step_id")
    val step: Step,

    @ManyToOne
    @JoinColumn(name = "member_test_id")
    val memberTest: MemberTest,

    @OneToMany(mappedBy = "report", orphanRemoval = true)
    var reportMedia: MutableList<ReportMedia> = mutableListOf(),

    ) {
}