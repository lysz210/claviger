package it.lysz210.akasha.claviger.infrastructure.intervals

import icu.intervals.api.v1.IntervalsOauthClient
import icu.intervals.api.v1.dto.AuthorizationCodeRequest
import io.smallrye.mutiny.Uni
import it.lysz210.akasha.claviger.domain.model.Credential
import it.lysz210.akasha.claviger.infrastructure.config.IntervalsProperties
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.UriBuilder
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.net.URI

@ApplicationScoped
class IntervalsOauthClientWrapper(
    @param:RestClient private val client: IntervalsOauthClient,
    private val mapper: IntervalOauthClientMapper,
    properties: IntervalsProperties,
) {
    private val baseUrl: String = properties.baseUrl()
    private val clientId: String = properties.oauth().clientId()
    private val clientSecret: String = properties.oauth().secret()

    fun getAuthorizeUri(callbackUri: URI): Uni<URI> {
        val authorizeUri = UriBuilder
            .fromUri(this.baseUrl).path("/oauth/authorize")
            .queryParam("client_id", this.clientId)
            .queryParam("redirect_uri", callbackUri.toString())
            .queryParam("scope", "ACTIVITY")
        .build()
        return Uni.createFrom().item(authorizeUri)
    }

    fun exchange(code: String): Uni<Credential> {
        val codeRequest = AuthorizationCodeRequest(
            code = code,
            clientId = clientId,
            clientSecret = clientSecret,
        )
        return this.client.token(codeRequest)
            .map { mapper.toDomain(
                clientId = clientId,
                authorizationCodeResponse = it,
            ) }
    }
}