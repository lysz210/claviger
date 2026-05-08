package it.lysz210.akasha.claviger.claviger.strava

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "strava")
interface StravaConfig {
    fun baseUrl(): String

    fun oauth(): Oauth

    interface Oauth {
        fun clientId(): String
        fun secret(): String
    }
}