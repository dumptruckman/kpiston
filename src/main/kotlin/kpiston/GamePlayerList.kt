package kpiston

import com.dumptruckman.bukkit.metadata.BukkitMetadata
import kpiston.events.PlayerJoinGameEvent
import kpiston.events.PlayerLeaveGameEvent
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