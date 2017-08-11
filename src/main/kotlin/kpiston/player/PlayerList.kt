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

import kpiston.Game
import kpiston.JoinResult
import kpiston.LeaveReason
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

abstract class PlayerList<G : Game<G>> protected constructor (val game: G) : Iterable<GamePlayer<out G>> {

    protected val players = mutableMapOf<UUID, GamePlayer<out G>>()

    val count: Int
        get() = players.size

    abstract fun addPlayer(player: Player): JoinResult

    protected abstract fun onRemove(player: OfflinePlayer, reason: LeaveReason)

    fun removePlayer(player: OfflinePlayer, reason: LeaveReason = LeaveReason.UNKNOWN) {
        if (players.remove(player.uniqueId) != null) {
            onRemove(player, reason)
        }
    }

    operator fun get(player: OfflinePlayer) = getPlayer(player)

    fun getPlayer(player: OfflinePlayer) = players[player.uniqueId]

    operator fun contains(player: OfflinePlayer) = players.containsKey(player.uniqueId)

    override fun iterator(): Iterator<GamePlayer<out G>> = players.values.iterator()

    fun removeAllPlayers(reason: LeaveReason = LeaveReason.UNKNOWN) {
        for (player in players) {
            onRemove(Bukkit.getOfflinePlayer(player.key), reason)
        }
        players.clear()
    }

    fun isEmpty() = players.isEmpty()
    fun isNotEmpty() = players.isNotEmpty()
}