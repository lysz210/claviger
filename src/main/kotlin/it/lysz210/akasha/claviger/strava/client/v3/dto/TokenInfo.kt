package it.lysz210.akasha.claviger.strava.client.v3.dto

import com.fasterxml.jackson.annotation.JsonProperty

@JvmRecord
data class TokenInfo (
    @get:JsonProperty("expires_at")
    val expiresAt: Long,

    @get:JsonProperty("expires_in")
    val expiresIn: Int,

    @get:JsonProperty("refresh_token")
    val refreshToken: String,

    @get:JsonProperty("access_token")
    val accessToken: String
)
