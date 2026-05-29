package it.lysz210.akasha.claviger.api.dto

import java.time.Instant

@JvmRecord
data class CredentialResponse(
    val group: String,
    val id: String,
    val expiresAt: Instant?,
    val status: CredentialStatus,
)
