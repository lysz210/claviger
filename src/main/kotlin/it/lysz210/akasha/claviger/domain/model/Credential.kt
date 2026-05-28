package it.lysz210.akasha.claviger.domain.model

@JvmRecord
data class Credential(
    val key: Key,
    val authentication: Authorization,
)