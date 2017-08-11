package kpiston.events

import kpiston.Game
import kpiston.JoinGameResult
import kpiston.extensions.isNotCancelled
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList

class PlayerJoinGameEvent(game: Game<*>, val player: Player)
    : GameEvent(game), Cancellable by CancellableSupplier() {

    val result: JoinGameResult
        get() {
            if (isHandled) {
                if (isNotCancelled) {
                    return JoinGameResult.SUCCESS
                } else {
                    return JoinGameResult.DENIED
                }
            } else {
                return JoinGameResult.NOT_ACCEPTING_PLAYERS
            }
        }

    override fun getHandlers() = Companion.handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = Companion.handlers
    }
}