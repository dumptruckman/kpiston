package kpiston.events

import kpiston.Game
import kpiston.LeaveGameReason
import org.bukkit.OfflinePlayer
import org.bukkit.event.HandlerList

class PlayerLeaveGameEvent(game: Game<*>, val player: OfflinePlayer, val reason: LeaveGameReason)
    : GameEvent(game) {

    override fun getHandlers() = Companion.handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = Companion.handlers
    }
}