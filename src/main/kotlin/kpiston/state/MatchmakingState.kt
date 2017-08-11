/*
 * This file is part of kpiston.
 *
 * Copyright (c) 2017 Jeremy Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package kpiston.state

import kpiston.Game
import kpiston.event.MatchmakingCompleteEvent
import kpiston.event.PlayerJoinGameEvent
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