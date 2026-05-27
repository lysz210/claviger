package it.lysz210.akasha.claviger.strava

import java.time.Instant

@JvmRecord
data class StravaTokenInfo (
    val id: String,
    val firstname: String?,
    val lastname: String?,
    val tokenType: String,
    val scope: String,
    val expiresAt: Instant,
    val accessToken: String
)
