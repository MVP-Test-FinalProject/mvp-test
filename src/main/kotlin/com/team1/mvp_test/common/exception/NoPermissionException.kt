package com.team1.mvp_test.common.exception

data class NoPermissionException(override val message:String = "보고서 작성 권한이 없습니다"): RuntimeException()
