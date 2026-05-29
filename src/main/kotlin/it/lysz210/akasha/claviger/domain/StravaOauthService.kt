package it.lysz210.akasha.claviger.domain

import io.smallrye.mutiny.Uni
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
    val credential: Uni<Credential> get() =
        this.credentialsStore.get(this.credentialsStore.stravaKey)

    fun getAuthorizeUri(callbackUri: URI): Uni<URI?> =
        this.credential
            .replaceWith(null as URI?)
            .onFailure().recoverWithUni { _ -> this.stravaClient.getAuthorizeUri(callbackUri) }

    fun exchangeCodeForAccessToken(code: String): Uni<Credential> =
        this.stravaClient.exchange(code)
            .call { response ->
                credentialsStore.put(response)
            }

    fun refreshToken(): Uni<Credential> =
        this.credential.onItem()
            .transformToUni{ this.stravaClient.refreshToken(it) }
            .onItem().call { response -> credentialsStore.put(response) }

    fun revoke(): Uni<String> =
        this.credential.map { it.key }
            .onItem().transformToUni { this.credentialsStore.remove(it) }
            .map { "Strava Token REVOKED!" }
            .onFailure().recoverWithItem { _ -> "Strava Token REVOKED!" }

}