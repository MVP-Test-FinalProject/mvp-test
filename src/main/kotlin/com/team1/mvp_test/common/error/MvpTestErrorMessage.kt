package com.team1.mvp_test.common.error

enum class MvpTestErrorMessage(val message: String) {
    TEST_ALREADY_FULL("Test is already full"),
    NOT_TEST_MEMBER("This member is not testing member"),
    NOT_AUTHORIZED("해당 테스트에 접근 권한이 없습니다"),
}