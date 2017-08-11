package kpiston.extensions

import kpiston.metadata.BukkitMetadata
import kpiston.metadata.Metadata
import org.bukkit.entity.Entity

val Entity.metadata: Metadata
    get() = BukkitMetadata.strongMetadataStore.getMetadata(uniqueId)