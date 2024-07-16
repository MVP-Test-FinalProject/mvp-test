package com.team1.mvp_test.domain.mvptest.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "mvp_test")
@Entity
class MvpTest(
    @Id
    @GeneratedValue()
    val id: Long? = null,

    @Column(name = "mvp_name")
    var mvpName: String,

    @Column(name = "recruit_start_date")
    var recruitStartDate: LocalDateTime,

    @Column(name = "recruit_ent_date")
    var recruitEndDate: LocalDateTime,

    @Column(name = "test_start_date")
    var testStartDate: LocalDateTime,

    @Column(name = "test_end_date")
    var testEndDate : LocalDateTime,

    @Column(name= "main_image_url")
    var mainImageUrl : String,

    @Column(name = "mvp_info" )
    var mvpInfo :String,

    @Column(name = "mvp_url")
    var mvpUrl : String,

    @Column(name = "reword_budget")
    var rewardBudget : Int,

    @Column(name = "requirement_min_age")
    var requirementMinAge : Int?,

    @Column(name = "requirement_max_age")
    var requirementMaxAge : Int?,

    @Column(name = "requriement_sex")
    var requirementSex : Boolean,

    @Column(name = "recruit_type")
    var recruitType : String,

    @Column(name = "recruit_num")
    var recruitNum : Long,

    @Column(name = "state")
    var state : String,

    @Column(name = "reject_reason")
    var rejectReason : String

    ) {

}