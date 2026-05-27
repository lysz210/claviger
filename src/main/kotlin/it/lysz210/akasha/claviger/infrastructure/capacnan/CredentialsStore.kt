package it.lysz210.akasha.claviger.infrastructure.capacnan

import io.quarkiverse.reactive.messaging.nats.jetstream.client.Client
import io.smallrye.mutiny.Uni
import it.lysz210.akasha.capacnan.quipus.credentials.CredentialQuipu
import it.lysz210.akasha.claviger.domain.model.Credential
import it.lysz210.akasha.claviger.domain.model.Key
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CredentialsStore(
    private val capacnanProperties: CapacnanProperties,
    private val natsClient: Client,
    private val credentialsQuipucamayoc: CredentialsQuipucamayoc,
) {

    fun put(credential: Credential): Uni<Long> {
        return natsClient.putValue(
            capacnanProperties.buckets().credentials(),
            credential.key.qualifiedId,
            credentialsQuipucamayoc.tie(credential)
        )
    }

    fun get(key: Key): Uni<Credential> {
        return natsClient.getValue(capacnanProperties.buckets().credentials(), key.qualifiedId, CredentialQuipu::class.java)
            .onItem().transform { credential -> credentialsQuipucamayoc.untie(credential) }
    }
}