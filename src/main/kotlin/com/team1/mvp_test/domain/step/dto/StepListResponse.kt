package com.team1.mvp_test.domain.step.dto

import com.team1.mvp_test.domain.step.model.Step

data class StepListResponse(
    val stepId :Long,
    val testId :Long,
    val title :String,
    val stepOrder : Int,
    val reward :Int,
){
    companion object{
        fun from(step : Step) : StepListResponse {
            return StepListResponse(
                stepId = step.id!!,
                testId = step.mvpTest.id!!,
                title = step.title,
                stepOrder = step.stepOrder,
                reward = step.reward,
            )
        }
    }
}
