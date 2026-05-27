package it.lysz210.akasha.claviger.domain.model

@JvmRecord
data class Athlete(
    val id: String,
    val firstname: String? = null,
    val lastname: String? = null,
)
