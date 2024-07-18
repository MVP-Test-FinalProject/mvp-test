package com.team1.mvp_test.domain.mvptest.constant

enum class RecruitType {
    FIRST_COME, DIRECT_SELECT;

    companion object {
        fun fromString(str : String): RecruitType {
            return runCatching {
                RecruitType.valueOf(str.uppercase())
            }.getOrElse { throw IllegalArgumentException("$str can't be recruit type.") }
        }
    }
}



