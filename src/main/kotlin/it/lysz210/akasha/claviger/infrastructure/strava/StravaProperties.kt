package it.lysz210.akasha.claviger.infrastructure.strava

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "strava")
interface StravaProperties {
    fun baseUrl(): String

    fun oauth(): Oauth

    interface Oauth {
        fun clientId(): String
        fun secret(): String
    }
}