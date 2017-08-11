package kpiston

import com.dumptruckman.bukkit.metadata.Metadata
import com.dumptruckman.bukkit.metadata.SimpleMetadata
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

/**
 * A basic player of a single game instance.
 */
class GamePlayer<G : Game<G>> (
        /**
         * The game instance this player is part of.
         */
        val game: G,
        player: OfflinePlayer) {

    val uniqueId = player.uniqueId

    /**
     * The online [Player] object for this game player, or null if they are not online.
     */
    val onlinePlayer: Player?
        get() = Bukkit.getPlayer(uniqueId)

    val offlinePlayer: OfflinePlayer
        get() = Bukkit.getOfflinePlayer(uniqueId)

    val metadata: Metadata by lazy {
        SimpleMetadata()
    }
}