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

import kpiston.arena.Arena
import kpiston.event.GameEvent
import kpiston.player.GamePlayerList
import kpiston.player.PlayerList
import kpiston.state.GameState
import kpiston.state.MatchmakingState
import kpiston.util.ConfigSupplier
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

/**
 * A Minecraft minigame.
 *
 * This class primarily manages the game states of the minigame. It provides a game state stack which can be pushed
 * to and popped from. General control of game states is done using the [KClass] objects of each [GameState]
 * implementation as keys.
 *
 * @constructor Constructs a new minigame in a uninitialized and unstarted state.
 *
 * All of the game states will be constructed at this point using [gameStateSuppliers]
 *
 * @param plugin The plugin that provides this minigame.
 */
abstract class Game<SELF : Game<SELF>>(
        /**
         * The plugin that provides this minigame.
         */
        val manager: GamesManager<*>) : Listener, Plugin by manager.plugin {

    val plugin = manager.plugin

    private val configurator = ConfigSupplier("games/${this::class.java.simpleName}.yml", manager.plugin)

    private var setup: Boolean = false
    private var tornDown: Boolean = false

    /**
     * A list of the suppliers that will provide instances of each [GameState] for this game.
     */
    protected abstract val gameStateSuppliers: List<() -> GameState<SELF>>
    /**
     * The initial game state that the game will push to the game state stack when the game is started.
     *
     * The initial state should be an implementation of [MatchmakingState].
     */
    protected abstract val initialState: KClass<out MatchmakingState<SELF>>
    /**
     * The arena the minigame takes place in.
     */
    abstract val arena: Arena<*>

    /**
     * The list of players that have joined this game.
     */
    @Suppress("UNCHECKED_CAST")
    val players: PlayerList<SELF> = GamePlayerList(this as SELF)
    /**
     * The stack of game states for this game.
     */
    val stateStack by lazy {
        @Suppress("UNCHECKED_CAST")
        (kpiston.state.StateStack<SELF>(this as SELF, gameStateSuppliers))
    }

    private fun setUp() {
        if (!setup) {
            setup = true
            manager.plugin.server.pluginManager.registerEvents(this, this)
            arena.setUp()
            stateStack.allStates.forEach { it.setUp() }
        }
    }

    private fun tearDown() {
        if (!tornDown) {
            tornDown = true
            HandlerList.unregisterAll(this as Plugin)
            stateStack.allStates.forEach { it.tearDown() }
            arena.tearDown()
        }
    }

    /**
     * Sets up the game and pushes the initial game state to the game state stack.
     *
     * All [GameState]s belonging to this game will be set up as part of this process.
     */
    fun startGame() {
        if (!setup && !tornDown) {
            setUp()
            stateStack.push(initialState)
        }
    }

    /**
     * Tears down the game and pops all of the game states off of the game state stack.
     *
     * All [GameStates]s belonging to this game will be torn down as part of this process.
     */
    fun endGame() {
        if (setup && !tornDown) {
            while (stateStack.isNotEmpty()) {
                stateStack.pop()
            }
            tearDown()
        }
    }

    /**
     * Returns whether this [GameState] is active or not.
     */
    override fun isEnabled() = setup && !tornDown

    /**
     * Gets the configuration for this GameState.
     *
     * Note that this configuration object cannot be saved to a file.
     */
    override fun getConfig() = configurator.getConfig()

    /**
     * Saves the raw contents of this game state's default config file to the location retrievable by [getConfig].
     *
     * This should fail silently if the config file already exists.
     */
    override fun saveDefaultConfig() = configurator.saveDefaultConfig()

    override fun reloadConfig() = configurator.reloadConfig()

    /**
     * This method is unsupported for [GameState] since multiple instances of this GameState may exist and may be
     * modifying the configuration separately.
     */
    override fun saveConfig() = configurator.saveConfig()

    /**
     * Indicates whether this game owns (initiated) the given event.
     *
     * A game and its states should only care about events it (or its states) initiated.
     *
     * See [Game.ignores].
     */
    infix fun owns(event: GameEvent<*>) = event.isOwnedBy(this)
    /**
     * Indicates whether this game should ignore the given event.
     *
     * A game and its states should only care about events it (or its states) initiated.
     */
    infix fun ignores(event: GameEvent<*>) = event.isNotOwnedBy(this)
}