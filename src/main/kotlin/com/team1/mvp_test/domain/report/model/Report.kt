package com.team1.mvp_test.domain.report.model

import com.team1.mvp_test.domain.member.model.MemberTest
import com.team1.mvp_test.domain.report.dto.UpdateReportRequest
import com.team1.mvp_test.domain.step.model.Step
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "report")
class Report(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Size(min = 5, max = 50, message = "1")
    @Column(name = "title")
    var title: String,

    @field:Size(min = 5, max = 500, message = "2")
    @Column(name = "body")
    var body: String,

    @field:Size(min = 5, max = 500, message = "3")
    @Column(name = "feedback")
    var feedback: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    var state: ReportState = ReportState.PENDING,

    @field:Size(min = 5, max = 500, message = "4")
    @Column(name = "reason")
    var reason: String? = null,

    @ManyToOne
    @JoinColumn(name = "step_id")
    val step: Step,

    @ManyToOne
    @JoinColumn(name = "member_test_id")
    val memberTest: MemberTest,

    @OneToMany
    @JoinColumn(name = "report_id")
    var reportMedia: MutableList<ReportMedia> = mutableListOf(),

    ) {
    fun addReportMedia(media: ReportMedia) {
        reportMedia.add(media)
    }

    fun clearReportMedia() {
        reportMedia.clear()
    }

    fun updateReport(request: UpdateReportRequest) {
        title = request.title
        body = request.body
        feedback = request.feedback
    }

    fun approve() {
        state = ReportState.APPROVED
    }
}