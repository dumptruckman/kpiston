package kpiston.arena

import kpiston.Game
import kpiston.metadata.Metadata
import kpiston.metadata.SimpleMetadata
import org.bukkit.Location

abstract class Arena<G : Game<G>>(val game: G, val name: CharSequence) {

    val metadata: Metadata = SimpleMetadata()

    abstract fun setUp()

    abstract fun tearDown()

    abstract val primarySpawn: Location
}