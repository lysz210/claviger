package it.lysz210.akasha.claviger.strava.persistence

@JvmRecord
data class StravaTokenEntry(
    val tokenType: String,
    val tokenInfo: TokenInfo,
    val scope: String,
    val athlete: Athlete,
)

@JvmRecord
data class Athlete(
    val id: String,
    val firstname: String?,
    val lastname: String?,
)

@JvmRecord
data class TokenInfo (
    val expiresAt: Int,
    val expiresIn: Int,
    val refreshToken: String,
    val accessToken: String
)