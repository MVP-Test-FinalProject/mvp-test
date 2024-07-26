package com.team1.mvp_test.common.error

enum class MvpTestErrorMessage(val message: String) {
    TEST_ALREADY_FULL("테스트 정원이 마감되었습니다"),
    NOT_TEST_MEMBER("해당 테스트에 참여한 인원이 아닙니다"),
    NOT_AUTHORIZED("해당 테스트에 접근 권한이 없습니다"),
    RECRUIT_DATE_NOT_VALID("테스트 모집 일자가 유효하지 않습니다"),
    TEST_DATE_NOT_VALID("테스트 참여 일자가 유효하지 않습니다"),
    AGE_RULE_INVALID("테스트 참여 나이 제한이 유효하지 않습니다"),
    MAIN_URL_NOT_EXIST("메인 이미지는 1개 필수입니다."),
}