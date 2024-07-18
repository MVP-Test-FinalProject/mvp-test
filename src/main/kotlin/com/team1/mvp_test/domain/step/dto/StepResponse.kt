package com.team1.mvp_test.domain.step.dto

import com.team1.mvp_test.domain.step.model.Step

data class StepResponse(
    val stepId : Long,
    val testId : Long,
    val title : String,
    val requirement : String,
    val guidelineUrl :String,
    val stepOrder : Int,
    val reward : Int,
){
    companion object{
        fun from(step : Step) : StepResponse {
            return StepResponse(
               stepId = step.id!!,
               testId = step.mvpTest.id!!,
               title = step.title,
               requirement = step.requirement,
               guidelineUrl = step.guidelineUrl,
                stepOrder = step.stepOrder,
               reward = step.reward,
            )
        }
    }
}