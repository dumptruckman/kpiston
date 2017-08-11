package kpiston.extensions

import com.dumptruckman.bukkit.metadata.BukkitMetadata
import com.dumptruckman.bukkit.metadata.Metadata
import org.bukkit.entity.Entity

val Entity.metadata: Metadata
    get() = BukkitMetadata.strongMetadataStore.getMetadata(uniqueId)