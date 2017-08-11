package kpiston

import com.dumptruckman.bukkit.metadata.Metadata
import com.dumptruckman.bukkit.metadata.SimpleMetadata
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/**
 * A basic player of a single game instance.
 */
class GamePlayer<T : Game<out Plugin>>(
        /**
         * The game instance this player is part of.
         */
        val game: Game<out Plugin>,
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