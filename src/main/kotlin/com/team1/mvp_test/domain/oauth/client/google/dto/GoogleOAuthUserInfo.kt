package com.team1.mvp_test.domain.oauth.client.google.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.team1.mvp_test.domain.oauth.client.OAuthLoginUserInfo
import com.team1.mvp_test.domain.oauth.provider.OAuthProvider

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class GoogleOAuthUserInfo(
    @JsonProperty("resultcode")
    val resultcode: String,
    val message: String,
    val response: GoogleUserInfoProperties
):OAuthLoginUserInfo(
    provider = OAuthProvider.GOOGLE,
    id = response.id,
    email = response.email
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class GoogleUserInfoProperties(
    val id: String,
    val name: String,
    val email: String,
)