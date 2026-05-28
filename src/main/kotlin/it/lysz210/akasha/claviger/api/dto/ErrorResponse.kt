package it.lysz210.akasha.claviger.api.dto

@JvmRecord
data class ErrorResponse(
    val message: String,
    val code: String,
)