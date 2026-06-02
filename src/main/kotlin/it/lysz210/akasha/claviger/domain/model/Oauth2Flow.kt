package it.lysz210.akasha.claviger.domain.model

import java.time.Instant

@JvmRecord
data class Oauth2Flow(
    val accessToken: String,
    val tokenType: String,
    val scope: String,
    val refreshToken: String? = null,
    val expiresAt: Instant? = null,
) {
    val expired: Boolean get() = expiresAt?.isBefore(Instant.now()) ?: false
}