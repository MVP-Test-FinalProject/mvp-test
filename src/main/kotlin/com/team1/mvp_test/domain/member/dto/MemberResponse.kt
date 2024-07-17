package com.team1.mvp_test.domain.member.dto

import com.team1.mvp_test.domain.member.model.Member

data class MemberResponse(
    val id: Long,
    val name: String?,
    val email: String?,
    val age: Long?,
    val sex: String?,
    val info: String?,
    val signupState: Boolean,
) {
    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                name = member.name,
                email = member.email,
                age = member.age,
                sex = member.sex,
                info = member.info,
                signupState = member.signUpState,
            )
        }
    }
}
