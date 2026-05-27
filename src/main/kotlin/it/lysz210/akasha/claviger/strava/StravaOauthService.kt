package it.lysz210.akasha.claviger.strava

import com.google.protobuf.timestamp
import io.nats.client.KeyValue
import io.smallrye.mutiny.Uni
import it.lysz210.akasha.capacnan.quipus.credentials.Quipucamayoc
import it.lysz210.akasha.capacnan.quipus.credentials.oauth2Flow
import it.lysz210.akasha.capacnan.quipus.credentials.secretData
import it.lysz210.akasha.claviger.strava.client.v3.StravaOauthV3
import it.lysz210.akasha.claviger.strava.client.v3.dto.Athlete
import it.lysz210.akasha.claviger.strava.client.v3.dto.AuthorizationCodeRequest
import it.lysz210.akasha.claviger.strava.client.v3.dto.AuthorizationRefreshRequest
import it.lysz210.akasha.claviger.strava.client.v3.dto.TokenInfo
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.UriBuilder
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.net.URI

@ApplicationScoped
class StravaOauthService (
    private val stravaProperties: StravaProperties,
    @param:RestClient private val stravaClient: StravaOauthV3,
    private val camayoc: Quipucamayoc,
    private val credentialsBucket: KeyValue
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
                .fromUri("${this.stravaProperties.baseUrl()}/oauth/authorize")
                .queryParam("client_id", this.stravaProperties.oauth().clientId())
                .queryParam("client_secret", this.stravaProperties.oauth().secret())
                .queryParam("redirect_uri", callbackUri.toString())
                .queryParam("response_type", "code")
                .queryParam("approval_prompt", "auto")
                .queryParam("scope", "read_all,activity:read_all")
                .build()
            Uni.createFrom().item(authorizeUri)
        }

    fun exchangeCodeForAccessToken(code: String): Uni<TokenInfo> {
        val clientId = this.stravaProperties.oauth().clientId()
        val codeRequest = AuthorizationCodeRequest(
            code = code,
            clientId = clientId,
            clientSecret = this.stravaProperties.oauth().secret(),
        )

        return this.stravaClient.token(codeRequest)
            .invoke { response ->
                val tokenInfo = response.tokenInfo
                this._token = tokenInfo
                this._athlete = response.athlete
                val data = secretData {
                    serviceId = clientId
                    oauth2Flow = oauth2Flow {
                        accessToken = tokenInfo.accessToken
                        refreshToken = tokenInfo.refreshToken
                        expiresAt = timestamp {
                            seconds = tokenInfo.expiresAt
                        }
                    }
                }
                val quipu = camayoc.tie("strava", data)
                credentialsBucket.put("strava.$clientId", quipu.toByteArray())
            }
            .map { response -> response.tokenInfo }
    }

    fun refreshToken(): Uni<TokenInfo> {
        val clientId = this.stravaProperties.oauth().clientId()
        val refreshRequest = AuthorizationRefreshRequest(
            clientId = clientId,
            clientSecret = this.stravaProperties.oauth().secret(),
            refreshToken = this._token?.refreshToken.toString()
        )
        return this.stravaClient.refresh(refreshRequest)
            .invoke { tokenInfo ->
                this._token = tokenInfo
                val data = secretData {
                    serviceId = clientId
                    oauth2Flow = oauth2Flow {
                        accessToken = tokenInfo.accessToken
                        refreshToken = tokenInfo.refreshToken
                        expiresAt = timestamp {
                            seconds = tokenInfo.expiresAt
                        }
                    }
                }
                val quipu = camayoc.tie("strava", data)
                credentialsBucket.put("strava.$clientId", quipu.toByteArray())
            }
    }

    fun revoke(): Uni<String> {
        this._token = null
        this._athlete = null
        return Uni.createFrom().item("Strava Token REVOKED!")
    }
}