package it.lysz210.akasha.claviger.infrastructure.capacnan

import io.smallrye.config.ConfigMapping
import it.lysz210.akasha.capacnan.blueprint.Blueprint

@ConfigMapping(prefix = "capacnan")
interface CredentialsBlueprint: Blueprint {

    fun credentials(): Credentials

    interface Credentials:
            Blueprint.Resources,
            Blueprint.WithKeyValue
}