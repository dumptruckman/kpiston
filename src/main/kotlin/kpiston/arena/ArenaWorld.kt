package kpiston.arena

import kpiston.Game
import kpiston.extensions.copy
import kpiston.extensions.delete
import kpiston.extensions.immutableLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator

class ArenaWorld<G : Game<G>>(game: G, name: CharSequence, private val templateName: CharSequence)
    : Arena<G>(game, name) {

    lateinit private var world: World

    override fun setUp() {
        val templateWorld = Bukkit.getWorld(templateName.toString())
        templateWorld?.copy(name) ?: throw IllegalStateException("Template world $templateName does not exist!")
        world = WorldCreator(name.toString()).copy(templateWorld).createWorld()
    }

    override fun tearDown() {
        world.delete()
    }

    override val primarySpawn: Location
        get() = world.spawnLocation

    fun test() {
        val t = world.getBlockAt(0, 0, 0).immutableLocation
    }
}