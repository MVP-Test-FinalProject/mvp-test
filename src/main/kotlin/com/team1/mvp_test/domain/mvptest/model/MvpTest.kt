package com.team1.mvp_test.domain.mvptest.model

import com.team1.mvp_test.domain.member.model.Sex
import com.team1.mvp_test.domain.mvptest.dto.UpdateMvpTestRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "mvp_test")
@Entity
class MvpTest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "enterprise_id")
    val enterpriseId: Long,

    @Column(name = "mvp_name")
    var mvpName: String,

    @Column(name = "recruit_start_date")
    var recruitStartDate: LocalDateTime,

    @Column(name = "recruit_ent_date")
    var recruitEndDate: LocalDateTime,

    @Column(name = "test_start_date")
    var testStartDate: LocalDateTime,

    @Column(name = "test_end_date")
    var testEndDate: LocalDateTime,

    @Column(name = "main_image_url")
    var mainImageUrl: String,

    @Column(name = "mvp_info")
    var mvpInfo: String,

    @Column(name = "mvp_url")
    var mvpUrl: String,

    @Column(name = "reword_budget")
    var rewardBudget: Int,

    @Column(name = "requirement_min_age")
    var requirementMinAge: Int?,

    @Column(name = "requirement_max_age")
    var requirementMaxAge: Int?,

    @Enumerated(EnumType.STRING)
    @Column(name = "requirement_sex")
    var requirementSex: Sex,

    @Column(name = "recruit_type")
    @Enumerated(EnumType.STRING)
    var recruitType: RecruitType,

    @Column(name = "recruit_num")
    var recruitNum: Int,

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    var state: MvpTestState,

    @Column(name = "reject_reason")
    var rejectReason: String? = null,


    ) {

    fun update(request: UpdateMvpTestRequest) {
        mvpName = request.mvpName
        recruitStartDate = request.recruitStartDate
        recruitEndDate = request.recruitEndDate
        testStartDate = request.testStartDate
        testEndDate = request.testEndDate
        mainImageUrl = request.mainImageUrl
        mvpInfo = request.mvpInfo
        mvpUrl = request.mvpUrl
        rewardBudget = request.rewardBudget
        requirementMinAge = request.requirementMinAge
        requirementMaxAge = request.requirementMaxAge
        requirementSex = request.requirementSex
        recruitType = request.recruitType
        recruitNum = request.recruitNum
    }

    init {
        validateRecruitDate()
        validateTestDate()
        validateAgeRule()
    }

    private fun validateRecruitDate() {
        require(recruitEndDate.isAfter(recruitStartDate) && recruitEndDate.isAfter(LocalDateTime.now())) {
            "모집 일자가 유효하지 않습니다."
        }
    }

    private fun validateTestDate() {
        require(
            testStartDate.isAfter(recruitStartDate) &&
                    testEndDate.isAfter(testStartDate) &&
                    testEndDate.isAfter(recruitEndDate)
        ) {
            "테스트 일자가 유효하지 않습니다."
        }
    }

    private fun validateAgeRule() {
        require(requirementMaxAge == null || requirementMinAge == null || requirementMaxAge!! > requirementMinAge!!) {
            "최대 나이는 최소 나이보다 큰 값이어야 합니다."
        }
    }

}