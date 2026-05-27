package it.lysz210.akasha.claviger.infrastructure.capacnan

import com.google.protobuf.timestamp
import it.lysz210.akasha.capacnan.quipus.credentials.CredentialQuipu
import jakarta.enterprise.context.ApplicationScoped
import it.lysz210.akasha.capacnan.quipus.credentials.Quipucamayoc
import it.lysz210.akasha.capacnan.quipus.credentials.oauth2Flow
import it.lysz210.akasha.capacnan.quipus.credentials.secretData
import it.lysz210.akasha.claviger.domain.model.Authorization
import it.lysz210.akasha.claviger.domain.model.Credential
import it.lysz210.akasha.claviger.domain.model.Key
import it.lysz210.akasha.claviger.domain.model.Oauth2Flow
import java.time.Instant

@ApplicationScoped
class CredentialsQuipucamayoc(
    private val camayoc: Quipucamayoc,
) {

    fun tie(data: Credential): CredentialQuipu {
        if (data.authentication.oauth2Flow != null) {
            val authentication = data.authentication
            return camayoc.tie(data.key.group, secretData {
                serviceId = data.key.id
                oauth2Flow = oauth2Flow {
                    accessToken = authentication.oauth2Flow.accessToken
                    refreshToken = authentication.oauth2Flow.refreshToken
                    expiresAt = timestamp {
                        seconds = authentication.oauth2Flow.expiresAt.epochSecond
                        nanos = authentication.oauth2Flow.expiresAt.nano
                    }
                }
            })
        }
        throw IllegalArgumentException("authentication not supported.")
    }

    fun untie(quipu: CredentialQuipu): Credential {
        val data = camayoc.untie(quipu)
        if (data.hasOauth2Flow()) {
            val oauth2Flow = data.oauth2Flow
            return Credential(
                key = Key(
                    group = quipu.userId,
                    id = data.serviceId,
                ),
                authentication = Authorization(
                    oauth2Flow = Oauth2Flow(
                        tokenType = oauth2Flow.tokenType,
                        accessToken = oauth2Flow.accessToken,
                        refreshToken = oauth2Flow.refreshToken,
                        expiresAt = Instant.ofEpochSecond(oauth2Flow.expiresAt.seconds, oauth2Flow.expiresAt.nanos.toLong()),
                        scope = oauth2Flow.scope,
                    )
                )
            )
        }
        throw IllegalArgumentException("unsupported.")
    }

}