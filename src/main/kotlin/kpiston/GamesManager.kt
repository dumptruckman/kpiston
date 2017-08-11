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
package kpiston

import kpiston.event.MatchmakingCompleteEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

class GamesManager<P : Plugin>(val plugin: P) : Listener {

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    private val inMatchmakingGames = mutableMapOf<KClass<out Game<*>>, GameQueue<*>>()
    private val inProgressGames = mutableMapOf<KClass<out Game<*>>, GameQueue<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <G : Game<G>> getMatchmakingQueue(type: KClass<out G>): GameQueue<G> = inMatchmakingGames.getOrPut(type, { GameQueue<G>() }) as GameQueue<G>
    @Suppress("UNCHECKED_CAST")
    fun <G : Game<G>> getInProgressQueue(type: KClass<out G>): GameQueue<G> = inProgressGames.getOrPut(type, { GameQueue<G>() }) as GameQueue<G>

    inline fun <reified G : Game<G>> createNewGame(gameSupplier: (GamesManager<P>) -> G): G {
        val game = gameSupplier(this)
        getMatchmakingQueue(G::class).add(game)
        game.startGame()
        return game
    }

    private fun <G : Game<G>> moveToInProgress(game: G) {
        val queue = getMatchmakingQueue(game::class)
        if (queue.removeIf({ g -> g === game })) {
            getInProgressQueue(game::class).add(game)
        }
    }

    fun <G : Game<G>> requestJoin(player: Player, type: KClass<G>): JoinResult {
        val queue = getMatchmakingQueue(type)
        if (queue.isEmpty()) return JoinResult.NO_GAME
        val game = queue.first

        return requestJoin(player, game)
    }

    fun <G : Game<G>> requestJoin(player: Player, game: G): JoinResult {
        if (player in game.players) {
            throw IllegalArgumentException("${player.name} is already in this game.")
        }
        return game.players.addPlayer(player)
    }

    @EventHandler
    private fun <G : Game<G>> matchmakingComplete(event: MatchmakingCompleteEvent<G>) {
        if (event.game.plugin == plugin) {
            if (event.game in getMatchmakingQueue(event.game::class)) {
                moveToInProgress(event.game)
            }
        }
    }
}