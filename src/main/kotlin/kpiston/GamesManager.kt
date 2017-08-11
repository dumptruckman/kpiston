package kpiston

import kpiston.events.MatchmakingCompleteEvent
import com.dumptruckman.endurance.EnduranceMiniGame
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

class GamesManager<P : Plugin>(val plugin: P) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    private val inMatchmakingGames = mutableMapOf<KClass<out Game<*>>, GameQueue<P>>()
    private val inProgressGames = mutableMapOf<KClass<out Game<*>>, GameQueue<P>>()

    fun getMatchmakingQueue(type: KClass<out Game<P>>) = inMatchmakingGames.getOrPut(type, { GameQueue(plugin) })
    fun getInProgressQueue(type: KClass<out Game<P>>) = inProgressGames.getOrPut(type, { GameQueue(plugin) })

    fun <G : Game<P>> createNewGame(gameSupplier: (P) -> G): G {
        val game = gameSupplier(plugin)
        getMatchmakingQueue(game::class).add(game)
        game.startGame()
        return game
    }

    private fun moveToInProgress(game: Game<P>) {
        val queue = getMatchmakingQueue(game::class)
        if (queue.removeIf({ g -> g === game })) {
            getInProgressQueue(game::class).add(game)
        }
    }

    fun requestJoin(player: Player, type: KClass<out Game<P>>): JoinGameResult {
        val queue = getMatchmakingQueue(type)
        if (queue.isEmpty()) return JoinGameResult.NO_GAME
        val game = queue.first

        return requestJoin(player, game)
    }

    fun requestJoin(player: Player, game: Game<P>): JoinGameResult {
        if (player in game.players) {
            throw IllegalArgumentException("${player.name} is already in this game.")
        }
        return game.players.addPlayer(player)
    }

    @EventHandler
    private fun matchmakingComplete(event: MatchmakingCompleteEvent) {
        if (event.game.plugin == plugin) {
            @Suppress("UNCHECKED_CAST")
            val game = event.game as Game<P>
            if (game in getMatchmakingQueue(game::class)) {
                moveToInProgress(event.game)
            }
        }
    }
}