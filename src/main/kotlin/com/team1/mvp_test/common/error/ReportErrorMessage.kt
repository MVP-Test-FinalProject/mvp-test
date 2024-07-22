package com.team1.mvp_test.common.error

enum class ReportErrorMessage(val message: String) {
    NOT_AUTHORIZED("보고서를 작성한 사용자가 아닙니다."),
    NOT_TEST_DURATION("테스트 작성 기간이 아닙니다."),
    NO_PERMISSION("보고서 작성 권한이 없습니다.")
}