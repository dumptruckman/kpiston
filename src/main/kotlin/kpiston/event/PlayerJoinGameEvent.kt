package kpiston.event

import kpiston.Game
import kpiston.JoinResult
import kpiston.extensions.isNotCancelled
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList

class PlayerJoinGameEvent<G : Game<G>>(game: G, val player: Player)
    : GameEvent<G>(game), Cancellable by CancellableSupplier() {

    val result: JoinResult
        get() {
            if (isHandled) {
                if (isNotCancelled) {
                    return JoinResult.SUCCESS
                } else {
                    return JoinResult.DENIED
                }
            } else {
                return JoinResult.NOT_ACCEPTING_PLAYERS
            }
        }

    override fun getHandlers() = Companion.handlers

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList() = Companion.handlers
    }
}