package com.team1.mvp_test.domain.step.model

import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.step.dto.UpdateStepRequest
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

@Table(name = "step")
@Entity
class Step(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title")
    @field:Size(min = 5, max = 20, message = "")
    var title: String,

    @Column(name = "requirement")
    @field:Size(min = 10, max = 200, message = "")
    var requirement: String,

    @Column(name = "guideline_url")
    var guidelineUrl: String? = null,

    @Column(name = "step_order")
    var stepOrder: Int,

    @Column(name = "reward")
    @field:Min(value = 1, message = "")
    var reward: Int,

    @ManyToOne
    @JoinColumn(name = "mvp_test_id")
    var mvpTest: MvpTest,

    ) {
    fun updateStep(request: UpdateStepRequest) {
        this.title = request.title
        this.requirement = request.requirement
        this.guidelineUrl = request.guidelineUrl

    }
}