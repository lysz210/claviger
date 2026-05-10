package it.lysz210.akasha.claviger.strava

import io.smallrye.mutiny.Uni
import it.lysz210.akasha.claviger.strava.client.StravaOauthV3
import it.lysz210.akasha.claviger.strava.client.dto.Athlete
import it.lysz210.akasha.claviger.strava.client.dto.AuthorizationCodeRequest
import it.lysz210.akasha.claviger.strava.client.dto.TokenInfo
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.UriBuilder
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.net.URI

@ApplicationScoped
class StravaOauthService (
    private val stravaConfig: StravaConfig,
    @RestClient private val stravaClient: StravaOauthV3,
) {
    private var _token: TokenInfo? = null
    private var _athlete: Athlete? = null
    val token: TokenInfo? get() = _token
    val athlete: Athlete? get() = _athlete.takeIf { _token != null }

    fun isAuthorized(): Boolean = this.token != null

    fun getAuthorizeUri(callbackUri: URI): Uni<URI> = if (this.isAuthorized()) {
            Uni.createFrom().nullItem()
        } else {
            val authorizeUri = UriBuilder
                .fromUri("${this.stravaConfig.baseUrl()}/oauth/authorize")
                .queryParam("client_id", this.stravaConfig.oauth().clientId())
                .queryParam("client_secret", this.stravaConfig.oauth().secret())
                .queryParam("redirect_uri", callbackUri.toString())
                .queryParam("response_type", "code")
                .queryParam("approval_prompt", "auto")
                .queryParam("scope", "read_all,activity:read_all")
                .build()
            Uni.createFrom().item(authorizeUri)
        }

    fun exchangeCodeForAccessToken(code: String): Uni<TokenInfo> {
        val codeRequest = AuthorizationCodeRequest(
            code = code,
            clientId = this.stravaConfig.oauth().clientId(),
            clientSecret = this.stravaConfig.oauth().secret(),
        )

        return this.stravaClient.token(codeRequest)
            .invoke { response ->
                this._token = response.tokenInfo
                this._athlete = response.athlete
            }
            .map { response -> response.tokenInfo }
    }

    fun revoke(): Uni<String> {
        this._token = null
        this._athlete = null
        return Uni.createFrom().item("Strava Token REVOKED!")
    }
}