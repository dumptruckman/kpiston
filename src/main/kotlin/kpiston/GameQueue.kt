package kpiston

import org.bukkit.plugin.Plugin
import java.util.ArrayDeque

class GameQueue<P : Plugin>(val plugin: P) {

    private val queue = ArrayDeque<Game<P>>()

    val size
        get() = queue.size
    val first
        get() = queue.first

    fun <G : Game<P>> add(game: G) = queue.add(game)
    fun isEmpty() = queue.isEmpty()
    fun removeIf(filter: (t: Game<P>) -> Boolean) = queue.removeIf(filter)

    operator fun <G : Game<P>> contains(game: G) = queue.contains(game)
}