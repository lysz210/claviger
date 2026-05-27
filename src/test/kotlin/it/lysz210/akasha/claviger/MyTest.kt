package it.lysz210.akasha.claviger

import io.quarkus.test.junit.QuarkusTest
import it.lysz210.akasha.capacnan.quipus.credentials.CredentialQuipu
import it.lysz210.akasha.capacnan.quipus.credentials.Quipucamayoc
import it.lysz210.akasha.capacnan.quipus.credentials.oauth2Flow
import it.lysz210.akasha.capacnan.quipus.credentials.secretData
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import kotlin.time.Clock

@QuarkusTest
class MyTest {
    @Inject lateinit var camayoc: Quipucamayoc

    @Test
    fun greeting() {

        val auth = oauth2Flow {
            accessToken = "access_token"
            expiresAt = Clock.System.now().toEpochMilliseconds()
        }

        val envelop: CredentialQuipu = camayoc.tie("me", secretData {
            serviceId = "service"
            oauth2Flow = auth
        })
        println(envelop)
        println(envelop.security.algo)

        println(camayoc.untie(envelop))

    }
}