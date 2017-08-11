package kpiston.player

import kpiston.Game
import kpiston.JoinResult
import kpiston.LeaveReason
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

// TODO Make this work for teams and games.
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