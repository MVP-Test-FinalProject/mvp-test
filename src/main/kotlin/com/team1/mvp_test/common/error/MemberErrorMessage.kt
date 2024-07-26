package com.team1.mvp_test.common.error

enum class MemberErrorMessage(val message: String) {
    EMAIL_ALREADY_IN_USE("이미 사용중인 이메일입니다."),
    PHONENUMBER_ALREADY_EXISTS("이미 사용중인 번호입니다."),
    PHONENUMBER_VERIFY_FAIL("인증에 실패한 번호입니다."),

}