package kpiston.extensions

import kpiston.metadata.BukkitMetadata
import kpiston.metadata.Metadata
import org.bukkit.metadata.Metadatable

val Metadatable.weakMetadata: Metadata
    get() = BukkitMetadata.weakMetadataStore.getMetadata(this)
