package kpiston.extensions

import com.dumptruckman.bukkit.metadata.BukkitMetadata
import com.dumptruckman.bukkit.metadata.Metadata
import kpiston.util.ImmutableStableLocation
import kpiston.util.StableLocation
import org.bukkit.block.Block

val Block.immutableLocation: ImmutableStableLocation
    get() = ImmutableStableLocation(this.world, this.x.toDouble(), this.y.toDouble(), this.z.toDouble())

val Block.stableLocation: StableLocation
    get() = StableLocation(this.world, this.x.toDouble(), this.y.toDouble(), this.z.toDouble())

val Block.metadata: Metadata
    get() = BukkitMetadata.strongMetadataStore.getMetadata(stableLocation)