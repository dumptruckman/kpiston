package kpiston.events

import kpiston.Game
import org.bukkit.event.HandlerList

class CountdownCompleteEvent<G : Game<G>>(game: G) : GameEvent<G>(game) {

    override fun getHandlers() = Companion.handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = Companion.handlers
    }
}