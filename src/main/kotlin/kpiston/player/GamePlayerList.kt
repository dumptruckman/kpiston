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
package kpiston.player

import kpiston.metadata.BukkitMetadata
import kpiston.Game
import kpiston.JoinResult
import kpiston.LeaveReason
import kpiston.event.PlayerJoinGameEvent
import kpiston.event.PlayerLeaveGameEvent
import kpiston.extensions.callEvent
import kpiston.extensions.metadata
import kpiston.metadata.PlayerKeys
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

internal class GamePlayerList<G : Game<G>>(game: G) : PlayerList<G>(game) {

    override fun addPlayer(player: Player): JoinResult {
        if (players.containsKey(player.uniqueId)) {
            throw IllegalArgumentException("${player.name} is already in this game.")
        }
        if (player.metadata[PlayerKeys.GAME_PLAYER] != null) {
            throw IllegalArgumentException("${player.name} is already in a game.")
        }

        val joinEvent = PlayerJoinGameEvent(game, player).callEvent()
        if (joinEvent.result == JoinResult.SUCCESS) {
            val gPlayer = GamePlayer<G>(game, player)
            player.metadata[PlayerKeys.GAME_PLAYER] = gPlayer
            players[player.uniqueId] = gPlayer
        }
        return joinEvent.result
    }

    override protected fun onRemove(player: OfflinePlayer, reason: LeaveReason) {
        PlayerLeaveGameEvent(game, player, reason).callEvent()
        BukkitMetadata.strongMetadataStore.getMetadata(player.uniqueId).remove(PlayerKeys.GAME_PLAYER)
    }

}