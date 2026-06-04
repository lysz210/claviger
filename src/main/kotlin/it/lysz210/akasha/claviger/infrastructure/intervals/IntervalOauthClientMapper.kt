package it.lysz210.akasha.claviger.infrastructure.intervals

import icu.intervals.api.v1.dto.AuthorizationCodeResponse
import it.lysz210.akasha.claviger.domain.model.Athlete
import it.lysz210.akasha.claviger.domain.model.Authorization
import it.lysz210.akasha.claviger.domain.model.Credential
import it.lysz210.akasha.claviger.domain.model.Key
import it.lysz210.akasha.claviger.domain.model.Oauth2Flow
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class IntervalOauthClientMapper {

    fun toDomain(
        authorizationCodeResponse: AuthorizationCodeResponse
    ): Oauth2Flow = Oauth2Flow(
        tokenType = authorizationCodeResponse.tokenType,
        accessToken = authorizationCodeResponse.accessToken,
        scope = authorizationCodeResponse.scope,
    )

    fun toDomain(clientId: String, authorizationCodeResponse: AuthorizationCodeResponse): Credential {
        return Credential(
            key = Key("intervals", clientId),
            authentication = Authorization(
                oauth2Flow = toDomain(
                    authorizationCodeResponse
                )
            ),
            athlete = Athlete(
                authorizationCodeResponse.athlete.id,
                authorizationCodeResponse.athlete.name,
            )
        )
    }
}