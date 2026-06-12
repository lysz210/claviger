package it.lysz210.akasha.claviger.infrastructure.capacnan

import io.quarkus.runtime.configuration.QuarkusConfigBuilderCustomizer
import io.smallrye.config.SmallRyeConfigBuilder

class CapacnanBlueprintUnsedConfigs: QuarkusConfigBuilderCustomizer() {
    override fun configBuilder(builder: SmallRyeConfigBuilder?) {
        builder?.withMappingIgnore("capacnan.credentials.**")
    }
}