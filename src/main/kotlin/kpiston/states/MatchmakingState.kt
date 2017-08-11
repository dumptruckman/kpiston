package kpiston.states

import kpiston.Game
import kpiston.GameState
import kpiston.events.MatchmakingCompleteEvent
import kpiston.events.PlayerJoinGameEvent
import kpiston.extensions.callEvent
import org.bukkit.ChatColor.RED
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.plugin.Plugin

abstract class MatchmakingState<T : Plugin>(game: Game<T>, val maxPlayers: Int) : GameState<T>(game) {

    @EventHandler(ignoreCancelled = true)
    fun playerJoin(event: PlayerJoinGameEvent) {
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