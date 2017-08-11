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
import kpiston.event.PlayerJoinTeamEvent
import kpiston.event.PlayerLeaveTeamEvent
import kpiston.extensions.callEvent
import kpiston.extensions.metadata
import kpiston.metadata.PlayerKeys
import kpiston.team.GameTeam
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

internal class TeamPlayerList<G : Game<G>>(val team: GameTeam<G>) : PlayerList<G>(team.game) {

    override fun addPlayer(player: Player): JoinResult {
        val gamePlayer = player.metadata[PlayerKeys.GAME_PLAYER]

        if (gamePlayer == null || gamePlayer.game != game) {
            throw IllegalArgumentException("${player.name} is attempting to join a team for a game they are not part of.")
        }
        if (players.containsKey(player.uniqueId)) {
            throw IllegalArgumentException("${player.name} is already on this team.")
        }
        if (player.metadata[PlayerKeys.GAME_TEAM] != null) {
            throw IllegalArgumentException("${player.name} is already on a team.")
        }

        val joinEvent = PlayerJoinTeamEvent(game, player, team).callEvent()
        if (joinEvent.result == JoinResult.SUCCESS) {
            player.metadata[PlayerKeys.GAME_TEAM] = team
            @Suppress("UNCHECKED_CAST")
            players[player.uniqueId] = gamePlayer as GamePlayer<out G>
        }
        return joinEvent.result
    }

    override protected fun onRemove(player: OfflinePlayer, reason: LeaveReason) {
        PlayerLeaveTeamEvent(game, player, reason).callEvent()
        BukkitMetadata.strongMetadataStore.getMetadata(player.uniqueId).remove(PlayerKeys.GAME_TEAM)
    }
}