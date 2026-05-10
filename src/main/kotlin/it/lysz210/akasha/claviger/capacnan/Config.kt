package it.lysz210.akasha.claviger.capacnan

import it.lysz210.akasha.capacnan.quipus.credentials.EncryptionStrategy
import it.lysz210.akasha.capacnan.quipus.credentials.NoopEncryptionStrategy
import it.lysz210.akasha.capacnan.quipus.credentials.Quipucamayoc
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class Config {

    @Produces
    @ApplicationScoped
    fun encryptionStrategy(): EncryptionStrategy = NoopEncryptionStrategy()

    @Produces
    @ApplicationScoped
    fun camayoc(strategy: EncryptionStrategy): Quipucamayoc = Quipucamayoc(strategy)
}