package it.lysz210.akasha.claviger.claviger.strava.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

@JvmRecord
data class TokenInfo (
    @get:JsonProperty("expires_at")
    val expiresAt: Int,

    @get:JsonProperty("expires_in")
    val expiresIn: Int,

    @get:JsonProperty("refresh_token")
    val refreshToken: String,

    @get:JsonProperty("access_token")
    val accessToken: String
)
