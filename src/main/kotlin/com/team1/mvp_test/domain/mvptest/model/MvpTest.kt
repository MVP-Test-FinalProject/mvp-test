package com.team1.mvp_test.domain.mvptest.model

import com.team1.mvp_test.domain.mvptest.constant.RecruitType
import com.team1.mvp_test.domain.mvptest.dto.mvptest.UpdateMvpTestRequest
import jakarta.persistence.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Table(name = "mvp_test")
@Entity
class MvpTest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "enterprise_id")
    val enterpriseId : Long,

    @field:NotBlank
    @field:Size(min=1, max = 50)
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

    @field:Min(10000)
    @Column(name = "reword_budget")
    var rewardBudget : Int,

    @field:Min(1)
    @field:Max(80)
    @Column(name = "requirement_min_age")
    var requirementMinAge : Int?,

    @Column(name = "requirement_max_age")
    var requirementMaxAge : Int?,

    @Column(name = "requirement_sex")
    var requirementSex : Boolean?,

    @field:Pattern(regexp = "FIRST_COME|DIRECT_SELECTION")
    @Column(name = "recruit_type")
    var recruitType : RecruitType,

    @field:Min(1)
    @Column(name = "recruit_num")
    var recruitNum : Long,

    @OneToMany
    var categories : List<CategoryMap>?
    ) {

    @Column(name = "state")
    lateinit var state : String

    @Column(name = "reject_reason")
    lateinit var rejectReason : String

    init {
        validateRecruitDate()
        validateTestDate()
        validateAgeRule()
        validateCategoriesRule()
    }

    fun update(request: UpdateMvpTestRequest, categoryMaps: List<CategoryMap>){
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
        recruitType = RecruitType.fromString(request.recruitType)
        recruitNum = request.recruitNum
        categories = categoryMaps

        validateRecruitDate()
        validateTestDate()
        validateAgeRule()
        validateCategoriesRule()
    }

    private fun validateRecruitDate(){
        require(recruitEndDate.isAfter(recruitStartDate)&&recruitEndDate.isAfter(LocalDateTime.now())){
            "Recruit end date is invalid"
        }
    }

    private fun validateTestDate(){
        require(testStartDate.isAfter(recruitStartDate)&&
            testEndDate.isAfter(testStartDate)&&
            testEndDate.isAfter(recruitEndDate)){
            "Test date is invalid"
        }
    }

    private fun validateAgeRule(){
        require(requirementMaxAge == null || requirementMinAge == null || requirementMaxAge!! > requirementMinAge!!) {
            "Maximum age must be greater than the minimum age."
        }
    }

    private fun validateCategoriesRule() {
        require(categories == null || categories!!.size <= 3) {
            "The number of categories can be specified up to 3."
        }
    }


}