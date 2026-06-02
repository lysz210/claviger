package icu.intervals.api.v1.dto

import com.fasterxml.jackson.annotation.JsonProperty

@JvmRecord
data class AuthorizationCodeResponse(
    @get:JsonProperty("token_type")
    val tokenType: String,
    @get:JsonProperty("access_token")
    val accessToken: String,
    val scope: String,
    val athlete: Athlete,
)
