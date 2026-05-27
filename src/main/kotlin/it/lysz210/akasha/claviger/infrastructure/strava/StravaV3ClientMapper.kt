package it.lysz210.akasha.claviger.infrastructure.strava

import com.strava.api.v3.dto.AuthorizationCodeResponse
import com.strava.api.v3.dto.TokenInfo
import it.lysz210.akasha.claviger.domain.model.*
import jakarta.enterprise.context.ApplicationScoped
import java.time.Instant

@ApplicationScoped
class StravaV3ClientMapper {

    fun toDomain(
        tokenType: String,
        tokenInfo: TokenInfo,
        scope: String,
    ): Oauth2Flow = Oauth2Flow(
        tokenType = tokenType,
        refreshToken = tokenInfo.refreshToken,
        accessToken = tokenInfo.accessToken,
        expiresAt = Instant.ofEpochSecond(tokenInfo.expiresIn.toLong()),
        scope = scope
    )
    fun toDomain(old: Credential, tokenInfo: TokenInfo): Credential {
        if (old.authentication.oauth2Flow == null) {
            throw RuntimeException()
        }
        return old.copy(authentication = Authorization(oauth2Flow = toDomain(
            old.authentication.oauth2Flow.tokenType,
            tokenInfo,
            old.authentication.oauth2Flow.scope
        )))
    }
    fun toDomain(clientId: String, authorizationCodeResponse: AuthorizationCodeResponse): Credential {
        return Credential(
            key = Key("strava", clientId),
            authentication = Authorization(
                oauth2Flow = toDomain(
                    authorizationCodeResponse.tokenType,
                    authorizationCodeResponse.tokenInfo,
                    authorizationCodeResponse.scope,
                )
            ),
            athlete = Athlete(
                id = authorizationCodeResponse.athlete.id,
                firstname = authorizationCodeResponse.athlete.firstname,
                lastname = authorizationCodeResponse.athlete.lastname,
            )
        )
    }


}