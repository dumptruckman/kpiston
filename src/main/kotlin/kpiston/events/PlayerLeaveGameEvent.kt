package kpiston.events

import kpiston.Game
import kpiston.LeaveReason
import org.bukkit.OfflinePlayer
import org.bukkit.event.HandlerList

class PlayerLeaveGameEvent<G : Game<G>>(game: G, val player: OfflinePlayer, val reason: LeaveReason)
    : GameEvent<G>(game) {

    override fun getHandlers() = Companion.handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = Companion.handlers
    }
}