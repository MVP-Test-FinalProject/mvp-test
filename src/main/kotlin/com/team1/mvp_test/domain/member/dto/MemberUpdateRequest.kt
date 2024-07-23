package com.team1.mvp_test.domain.member.dto

import jakarta.validation.constraints.Size

data class MemberUpdateRequest(
    @field:Size(min = 1)
    val name: String,

    @field:Size(max = 500)
    val info: String?,
)