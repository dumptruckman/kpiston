package kpiston

import com.dumptruckman.bukkit.metadata.BukkitMetadata
import kpiston.events.PlayerJoinTeamEvent
import kpiston.events.PlayerLeaveTeamEvent
import kpiston.extensions.callEvent
import kpiston.extensions.metadata
import kpiston.metadata.PlayerKeys
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

internal class TeamPlayerList<G : Game<G>>(game: G, val team: GameTeam<G>) : PlayerList<G>(game) {

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