package it.lysz210.akasha.claviger.infrastructure.config

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "intervals")
interface IntervalsProperties {
    fun baseUrl(): String
    fun oauth(): Oauth

    interface Oauth {
        fun clientId(): String
        fun secret(): String
    }
}