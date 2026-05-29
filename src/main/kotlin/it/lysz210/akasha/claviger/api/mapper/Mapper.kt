package it.lysz210.akasha.claviger.api.mapper

import it.lysz210.akasha.claviger.api.dto.CredentialResponse
import it.lysz210.akasha.claviger.api.dto.CredentialStatus
import it.lysz210.akasha.claviger.domain.model.Credential
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class Mapper {
    fun toDto(credential: Credential): CredentialResponse {
        val oauth2Flow = credential.authentication.oauth2Flow
        val status = when (oauth2Flow?.expired) {
            true -> CredentialStatus.EXPIRED
            false -> CredentialStatus.LOGGED_IN
            null -> CredentialStatus.UNKNOWN
        }
        return CredentialResponse(
            group = credential.key.group,
            id = credential.key.id,
            expiresAt = oauth2Flow?.expiresAt,
            status = status
        )
    }
}