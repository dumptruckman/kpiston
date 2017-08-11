package kpiston.extensions

import com.dumptruckman.bukkit.metadata.BukkitMetadata
import com.dumptruckman.bukkit.metadata.Metadata
import org.bukkit.metadata.Metadatable

val Metadatable.weakMetadata: Metadata
    get() = BukkitMetadata.weakMetadataStore.getMetadata(this)
