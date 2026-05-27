package it.lysz210.akasha.claviger.infrastructure.strava

import com.strava.api.v3.StravaOauthV3
import com.strava.api.v3.dto.AuthorizationCodeRequest
import com.strava.api.v3.dto.AuthorizationRefreshRequest
import io.smallrye.mutiny.Uni
import it.lysz210.akasha.claviger.domain.model.Credential
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.UriBuilder
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.net.URI

@ApplicationScoped
class StravaOauthV3Wrapper(
    @param:RestClient private val stravaClient: StravaOauthV3,
    private val mapper: StravaV3ClientMapper,
    private val stravaProperties: StravaProperties,
) {
    fun getAuthorizeUri(callbackUri: URI): Uni<URI> {
        val authorizeUri = UriBuilder
            .fromUri("${this.stravaProperties.baseUrl()}/oauth/authorize")
            .queryParam("client_id", this.stravaProperties.oauth().clientId())
            .queryParam("client_secret", this.stravaProperties.oauth().secret())
            .queryParam("redirect_uri", callbackUri.toString())
            .queryParam("response_type", "code")
            .queryParam("approval_prompt", "auto")
            .queryParam("scope", "read_all,activity:read_all")
            .build()
        return Uni.createFrom().item(authorizeUri)
    }

    fun exchange(code: String): Uni<Credential> {
        val clientId = this.stravaProperties.oauth().clientId()
        val codeRequest = AuthorizationCodeRequest(
            code = code,
            clientId = clientId,
            clientSecret = this.stravaProperties.oauth().secret(),
        )
        return this.stravaClient.token(codeRequest)
            .map { mapper.toDomain(
                clientId = clientId,
                authorizationCodeResponse = it
            ) }
    }

    fun refreshToken(current: Credential): Uni<Credential> {
        val clientId = this.stravaProperties.oauth().clientId()
        if (current.authentication.oauth2Flow == null) {
            throw RuntimeException()
        }
        val refreshRequest = AuthorizationRefreshRequest(
            clientId = clientId,
            clientSecret = this.stravaProperties.oauth().secret(),
            refreshToken = current.authentication.oauth2Flow.refreshToken
        )
        return this.stravaClient.refresh(refreshRequest)
            .map { mapper.toDomain(current, it) }
    }
}

