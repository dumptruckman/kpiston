package kpiston

import java.util.ArrayDeque

class GameQueue<G : Game<G>> {

    private val queue = ArrayDeque<G>()

    val size
        get() = queue.size
    val first: G
        get() = queue.first as G

    fun add(game: G) = queue.add(game)
    fun isEmpty() = queue.isEmpty()
    fun removeIf(filter: (t: Game<*>) -> Boolean) = queue.removeIf(filter)

    operator fun contains(game: Game<*>) = queue.contains(game)
}