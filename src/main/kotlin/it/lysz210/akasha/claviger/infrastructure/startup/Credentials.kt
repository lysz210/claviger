package it.lysz210.akasha.claviger.infrastructure.cron

import io.quarkus.logging.Log
import io.quarkus.runtime.StartupEvent
import it.lysz210.akasha.claviger.domain.StravaOauthService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes

@ApplicationScoped
class Credential(
    private val stravaOauthService: StravaOauthService,
) {
    fun onStartup(@Observes ev: StartupEvent) {
        stravaOauthService.refreshToken()
            .subscribe().with (
                { credential ->
                    Log.info("Credential ${credential.key} refreshed.")
                },
                { failure ->
                    Log.error("Failed to refresh Strava credential.", failure)
                }
            )
    }
}