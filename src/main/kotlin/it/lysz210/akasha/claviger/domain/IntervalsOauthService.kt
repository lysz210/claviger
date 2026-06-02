package it.lysz210.akasha.claviger.domain

import io.smallrye.mutiny.Uni
import it.lysz210.akasha.claviger.domain.model.Credential
import it.lysz210.akasha.claviger.infrastructure.capacnan.CredentialsStore
import it.lysz210.akasha.claviger.infrastructure.intervals.IntervalsOauthClientWrapper
import jakarta.enterprise.context.ApplicationScoped
import java.net.URI

@ApplicationScoped
class IntervalsOauthService (
    private val client: IntervalsOauthClientWrapper,
    private val credentialsStore: CredentialsStore,
) {
    val credential: Uni<Credential> get() =
        this.credentialsStore.get(this.credentialsStore.intervalsKey)

    fun getAuthorizeUri(callbackUri: URI): Uni<URI?> =
        this.credential
            .replaceWith(null as URI?)
            .onFailure().recoverWithUni { _ -> this.client.getAuthorizeUri(callbackUri) }

    fun exchangeCodeForAccessToken(code: String): Uni<Credential> =
        this.client.exchange(code)
            .call { response ->
                credentialsStore.put(response)
            }

    fun revoke(): Uni<String> =
        this.credential.map { it.key }
            .onItem().transformToUni { this.credentialsStore.remove(it) }
            .map { "Intervals Token REVOKED!" }
            .onFailure().recoverWithItem { _ -> "Intervals Token REVOKED!" }

}