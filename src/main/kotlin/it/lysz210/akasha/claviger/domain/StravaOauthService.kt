package it.lysz210.akasha.claviger.domain

import io.smallrye.mutiny.Uni
import it.lysz210.akasha.claviger.domain.model.Athlete
import it.lysz210.akasha.claviger.domain.model.Credential
import it.lysz210.akasha.claviger.infrastructure.capacnan.CredentialsStore
import it.lysz210.akasha.claviger.infrastructure.strava.StravaOauthV3Wrapper
import jakarta.enterprise.context.ApplicationScoped
import java.net.URI

@ApplicationScoped
class StravaOauthService (
    private val stravaClient: StravaOauthV3Wrapper,
    private val credentialsStore: CredentialsStore,
) {
    private var _credential: Credential? = null
    val credential: Credential? get() = this._credential
    val athlete: Athlete? get() = this._credential?.athlete

    fun isAuthorized(): Boolean = this.credential != null

    fun getAuthorizeUri(callbackUri: URI): Uni<URI> = if (this.isAuthorized()) {
            Uni.createFrom().nullItem()
        } else {
            stravaClient.getAuthorizeUri(callbackUri)
        }

    fun exchangeCodeForAccessToken(code: String): Uni<Credential> = this.stravaClient.exchange(code)
            .call { response ->
                this._credential = response
                credentialsStore.put(response)
            }

    fun refreshToken(): Uni<Credential> {
        val current = this._credential
        if (current != null) {
            return this.stravaClient.refreshToken(current)
                .call { response ->
                    this._credential = response
                    credentialsStore.put(response)
                }
        }
        throw RuntimeException()
    }

    fun revoke(): Uni<String> {
        this._credential = null
        return Uni.createFrom().item("Strava Token REVOKED!")
    }
}