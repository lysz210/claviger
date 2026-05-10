package it.lysz210.akasha.claviger.capacnan

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "capacnan")
interface CapacnanProperties {
    fun bucketName(): String
}