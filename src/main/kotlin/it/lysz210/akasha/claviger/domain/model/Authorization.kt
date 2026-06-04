package it.lysz210.akasha.claviger.domain.model

@JvmRecord
data class Authorization (
    val oauth2Flow: Oauth2Flow?
)