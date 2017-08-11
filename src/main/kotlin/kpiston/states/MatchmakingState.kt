package kpiston.states

import kpiston.Game
import kpiston.GameState
import kpiston.events.MatchmakingCompleteEvent
import kpiston.events.PlayerJoinGameEvent
import kpiston.extensions.callEvent
import org.bukkit.ChatColor.RED
import org.bukkit.GameMode
import org.bukkit.event.EventHandler

abstract class MatchmakingState<G : Game<G>>(game: G, val maxPlayers: Int) : GameState<G>(game) {

    @EventHandler(ignoreCancelled = true)
    fun playerJoin(event: PlayerJoinGameEvent<G>) {
        if (game ignores event) return

        event.isHandled = true

        if (game.players.count >= maxPlayers) {
            event.player.sendMessage("${RED}The game is full!")
            event.isCancelled = true
        } else {
            event.player.gameMode = GameMode.ADVENTURE
            event.player.teleport(game.arena.primarySpawn)

            if (game.players.count == maxPlayers - 1) {
                MatchmakingCompleteEvent(game).callEvent()
            }
        }
    }
}