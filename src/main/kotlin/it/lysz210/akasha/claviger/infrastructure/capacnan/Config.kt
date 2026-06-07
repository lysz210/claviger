package it.lysz210.akasha.claviger.infrastructure.capacnan

import io.nats.client.Connection
import io.nats.client.JetStreamApiException
import io.nats.client.KeyValue
import io.nats.client.api.KeyValueConfiguration
import io.nats.client.api.StorageType
import it.lysz210.akasha.capacnan.blueprint.CapacnanBlueprint
import it.lysz210.akasha.capacnan.quipus.credentials.EncryptionStrategy
import it.lysz210.akasha.capacnan.quipus.credentials.NoopEncryptionStrategy
import it.lysz210.akasha.capacnan.quipus.credentials.Quipucamayoc
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class Config (
    blueprint: CapacnanBlueprint
) {
    private val bucketName = blueprint.security().kv().bucket()

    @Produces
    @ApplicationScoped
    fun encryptionStrategy(): EncryptionStrategy = NoopEncryptionStrategy()

    @Produces
    @ApplicationScoped
    fun camayoc(strategy: EncryptionStrategy): Quipucamayoc = Quipucamayoc(strategy)

    @Produces
    @ApplicationScoped
    fun credentialsBucket(nats: Connection): KeyValue {

        try {
            nats.keyValueManagement().create(
                KeyValueConfiguration.builder()
                    .name(bucketName)
                    .storageType(StorageType.File)
                    .build()
            )
            return nats.keyValue(bucketName)
        } catch (e: JetStreamApiException) {
            if (e.errorCode == 400 && e.message?.contains("stream name already in use") == true) {
                return nats.keyValue(bucketName)
            } else {
                throw e
            }
        }
    }
}