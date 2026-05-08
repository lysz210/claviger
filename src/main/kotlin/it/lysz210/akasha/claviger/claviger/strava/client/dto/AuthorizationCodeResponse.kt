package it.lysz210.akasha.claviger.claviger.strava.client.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped

@JvmRecord
data class AuthorizationCodeResponse(
    @get:JsonProperty("token_type")
    val tokenType: String,
    @field:JsonUnwrapped
    val tokenInfo: TokenInfo,
    val scope: String,
    val athlete: Athlete,
)
