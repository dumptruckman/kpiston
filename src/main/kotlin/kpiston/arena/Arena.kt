package kpiston.arena

import kpiston.Game
import com.dumptruckman.bukkit.metadata.Metadata
import com.dumptruckman.bukkit.metadata.SimpleMetadata
import org.bukkit.Location
import org.bukkit.plugin.Plugin

abstract class Arena<G : Game<out Plugin>>(val game: G, val name: CharSequence) {

    val metadata: Metadata = SimpleMetadata()

    abstract fun setUp()

    abstract fun tearDown()

    abstract val primarySpawn: Location
}