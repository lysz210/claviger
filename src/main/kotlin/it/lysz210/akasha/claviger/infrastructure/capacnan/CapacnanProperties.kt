package it.lysz210.akasha.claviger.infrastructure.capacnan

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "capacnan")
interface CapacnanProperties {
    fun buckets(): Buckets
    interface Buckets {
        fun credentials(): String
    }
}