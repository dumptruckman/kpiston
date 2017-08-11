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
package kpiston.state

import kpiston.Game
import kpiston.util.ConfigSupplier
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.event.EventHandler

/**
 * A game state for a given game.
 *
 * GameStates may declare [EventHandler] methods and they will be automatically registered when the game starts.
 * In addition, event handling methods will also only be called while this GameState is active.
 *
 * Unfortunately, for this to work properly, GameState implements [Plugin]. However, the majority of its methods are
 * delegated to [Game.plugin]. The folowing are exceptions:
 *
 * * [isEnabled]
 * * [getConfig]
 * * [reloadConfig]
 * * [saveConfig]
 * * [saveDefaultConfig]
 *
 * Each separate GameState implementation class will have an associated configuration file located in
 * pluginDir/gamestates/ClassName.yml. Each GameState will have access to it's own [FileConfiguration] object for this
 * config file but it is important that game states do not attempt to save to that file since other instances of the
 * same GameState would not necessarily see these changes.
 *
 * @constructor Creates a new instance of the GameState in a non-initialized and non-active state.
 * @param game The minigame that this GameState belongs to.
 */
abstract class GameState<G : Game<G>>(val game: G) : Listener, Plugin by game.plugin {

    private val configurator = ConfigSupplier("states/${this::class.java.simpleName}.yml", game.plugin)

    private var setup = false
    private var tornDown = false
    private var enabled = false

    internal fun setUp() {
        enabled = true
        game.plugin.server.pluginManager.registerEvents(this, this)
        enabled = false
        onSetUp()
        setup = true
    }

    internal open fun tearDown() {
        onTearDown()
        HandlerList.unregisterAll(this as Plugin)
    }

    internal fun pause() {
        onPause()
        enabled = false
    }

    internal fun resume() {
        if (!setup) {
            throw IllegalStateException("GameState must be set up first.")
        }
        onResume()
        enabled = true
    }

    /**
     * This method should be implemented if anything needs to be done with this game state when the game suspends this
     * state.
     *
     * Game states are paused when they are popped from the [Game]s state stack.
     *
     * A suspended state can potentially be resumed later during the same game. Suspended states do not receive
     * events.
     */
    protected open fun onPause() { }

    /**
     * This method should be implemented if anything needs to be done with this game state when the game resumes this
     * state.
     *
     * Game states are resumed when they are pushed to the [Game]s state stack.
     */
    protected open fun onResume() { }

    /**
     * This method should be implemented if anything needs to be done for this game state to be ready for a new game.
     *
     * All game states for a [Game] will be set up when [Game.startGame] is called.
     */
    protected open fun onSetUp() { }

    /**
     * This method should be implemented if anything needs to be done with this game state at the end of a game.
     *
     * All game states for a [Game] will be torn down when [Game.endGame] is called.
     */
    protected open fun onTearDown() { }

    /**
     * Returns whether this [GameState] is active or not.
     */
    override fun isEnabled() = enabled

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
}