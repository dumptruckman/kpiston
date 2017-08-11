package kpiston

import com.dumptruckman.bukkit.metadata.BukkitMetadata
import kpiston.events.PlayerJoinGameEvent
import kpiston.events.PlayerLeaveGameEvent
import kpiston.extensions.callEvent
import kpiston.extensions.metadata
import kpiston.metadata.PlayerKeys
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID

// TODO Make this work for teams and games.
class PlayerList<G : Game<out Plugin>>(val game: G) : Iterable<GamePlayer<out G>> {

    private val players = mutableMapOf<UUID, GamePlayer<out G>>()

    val count: Int
        get() = players.size

    fun addPlayer(player: Player): JoinGameResult {
        if (players.containsKey(player.uniqueId)) {
            throw IllegalArgumentException("${player.name} is already in this game.")
        }
        if (player.metadata[PlayerKeys.GAME_PLAYER] != null) {
            throw IllegalArgumentException("${player.name} is already in a game.")
        }

        val joinEvent = PlayerJoinGameEvent(game, player).callEvent()
        if (joinEvent.result == JoinGameResult.SUCCESS) {
            val gPlayer = GamePlayer<G>(game, player)
            player.metadata[PlayerKeys.GAME_PLAYER] = gPlayer
            players[player.uniqueId] = gPlayer
        }
        return joinEvent.result
    }

    private fun remove(player: OfflinePlayer, reason: LeaveGameReason) {
        PlayerLeaveGameEvent(game, player, reason).callEvent()
        BukkitMetadata.strongMetadataStore.getMetadata(player.uniqueId).remove(PlayerKeys.GAME_PLAYER)
    }

    fun removePlayer(player: OfflinePlayer, reason: LeaveGameReason = LeaveGameReason.UNKNOWN) {
        if (players.remove(player.uniqueId) != null) {
            remove(player, reason)
        }
    }

    operator fun get(player: OfflinePlayer) = getPlayer(player)

    fun getPlayer(player: OfflinePlayer) = players[player.uniqueId]

    operator fun contains(player: OfflinePlayer) = players.containsKey(player.uniqueId)

    override fun iterator(): Iterator<GamePlayer<out G>> = players.values.iterator()

    fun removeAllPlayers(reason: LeaveGameReason = LeaveGameReason.UNKNOWN) {
        for (player in players) {
            remove(Bukkit.getOfflinePlayer(player.key), reason)
        }
        players.clear()
    }

    fun isEmpty() = players.isEmpty()
    fun isNotEmpty() = players.isNotEmpty()
}