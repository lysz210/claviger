package it.lysz210.akasha.claviger.infrastructure.capacnan

import com.google.protobuf.MessageLite
import com.google.protobuf.Parser
import io.quarkiverse.reactive.messaging.nats.jetstream.client.mapper.Serializer
import it.lysz210.akasha.capacnan.quipus.credentials.CredentialQuipu
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProtobufSerializer: Serializer {
    private val parsers = mapOf<Class<*>, Parser<*>>(
        CredentialQuipu::class.java to CredentialQuipu.parser()
    )
    override fun <T : Any?> readValue(data: ByteArray?, type: Class<T>?): T? {
        if (data == null) return null

        if (type == null) throw NullPointerException("type cannot be null.")

        @Suppress("UNCHECKED_CAST")
        val parser = parsers[type] as? Parser<T>
            ?: throw IllegalArgumentException("No registered parser found for type: ${type.name}")

        return try {
            parser.parseFrom(data)
        } catch (e: Exception) {
            throw RuntimeException("Failed to deserialize type: ${type.name}", e)
        }
    }

    override fun <T : Any?> toBytes(payload: T?): ByteArray? {
        if (payload == null) return null
        if (payload is MessageLite) {
            return payload.toByteArray()
        }
        throw IllegalArgumentException("ActivitySerializer expected Activity type but got ${payload::class.java.name}")
    }
}