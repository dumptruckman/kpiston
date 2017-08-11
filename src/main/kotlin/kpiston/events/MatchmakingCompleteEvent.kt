package kpiston.events

import kpiston.Game
import org.bukkit.event.HandlerList

class MatchmakingCompleteEvent(game: Game<*>) : GameEvent(game) {

    override fun getHandlers() = Companion.handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = Companion.handlers
    }
}