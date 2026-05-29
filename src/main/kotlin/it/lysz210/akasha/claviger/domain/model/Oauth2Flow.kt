package it.lysz210.akasha.claviger.domain.model

import java.time.Instant

@JvmRecord
data class Oauth2Flow(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Instant,
    val tokenType: String,
    val scope: String,
) {
    val expired: Boolean get() = expiresAt.isBefore(Instant.now())
}