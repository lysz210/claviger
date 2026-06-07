package it.lysz210.akasha.claviger.infrastructure.capacnan

import io.quarkiverse.reactive.messaging.nats.jetstream.client.Client
import io.smallrye.mutiny.Uni
import it.lysz210.akasha.capacnan.blueprint.CapacnanBlueprint
import it.lysz210.akasha.capacnan.quipus.credentials.CredentialQuipu
import it.lysz210.akasha.claviger.domain.exception.CredentialNotFoundException
import it.lysz210.akasha.claviger.domain.model.Credential
import it.lysz210.akasha.claviger.domain.model.Key
import it.lysz210.akasha.claviger.infrastructure.config.IntervalsProperties
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class CredentialsStore(
    private val natsClient: Client,
    private val credentialsQuipucamayoc: CredentialsQuipucamayoc,
    intervalsProperties: IntervalsProperties,
    blueprint: CapacnanBlueprint,
) {
    val bucketName = blueprint.security().kv().bucket()
    private val _intervalKey = Key("intervals", intervalsProperties.oauth().clientId())

    val intervalsKey: Key get() = this._intervalKey

    fun put(credential: Credential): Uni<Long> {
        return natsClient.putValue(
            this.bucketName,
            credential.key.qualifiedId,
            credentialsQuipucamayoc.tie(credential)
        )
    }

    fun get(key: Key): Uni<Credential> {
        return natsClient.getValue(this.bucketName, key.qualifiedId, CredentialQuipu::class.java)
            .onItem().transform { credential -> credentialsQuipucamayoc.untie(credential) }
            .onFailure().transform { CredentialNotFoundException(key) }
    }

    fun remove(key: Key) =
        natsClient.deleteValue(this.bucketName, key.qualifiedId)
            .onFailure().transform { CredentialNotFoundException(key) }
}