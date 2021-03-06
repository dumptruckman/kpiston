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

import kpiston.arena.ArenaWorld
import kpiston.state.MatchmakingState
import kpiston.util.Logging
import com.dumptruckman.bukkit.utils.TestEvent
import kpiston.state.GameState
import org.bukkit.event.EventHandler
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import kotlin.reflect.KClass

class StateStackTest {

    class InitialState(game: TestGame) : MatchmakingState<TestGame>(game, 12) {
        var eventCalled = false

        @EventHandler
        fun testEvent(event: TestEvent) {
            eventCalled = true
        }
    }
    class AnotherState(game: TestGame) : GameState<TestGame>(game) {
        var eventCalled = false

        @EventHandler
        fun testEvent(event: TestEvent) {
            eventCalled = true
        }
    }
    class OtherGameState(game: TestGame) : GameState<TestGame>(game)

    class TestGame(manager: GamesManager<*>) : Game<TestGame>(manager) {
        override val gameStateSuppliers: List<() -> GameState<TestGame>> = listOf(
                { InitialState(this) },
                { AnotherState(this) }
        )
        override val initialState: KClass<out MatchmakingState<TestGame>> = InitialState::class
        override val arena = ArenaWorld(this, "test1", "test")
    }

    object TestPlugin : com.dumptruckman.bukkit.utils.TestPlugin()

    var game: TestGame = TestGame(TestPlugin.gamesManager)

    @Before
    fun setUp() {
        Logging.init(TestPlugin)
        game = TestGame(TestPlugin.gamesManager)
    }

    @Test
    fun testGameCreation() {
        assertNull(game.stateStack.top)
        game.startGame()
        val topState = game.stateStack.top
        assertNotNull(topState)
        assertEquals(InitialState::class, topState!!::class)
    }

    @Test
    fun testPopWhileActiveStateExists() {
        game.startGame()
        assertNotNull(game.stateStack.top)
        assertNotNull(game.stateStack.pop())
    }

    @Test(expected = NoSuchElementException::class)
    fun testPopWhileNoActiveState() {
        assertNull(game.stateStack.top)
        game.stateStack.pop()
    }

    @Test
    fun testChangeState() {
        game.startGame()
        assertEquals(InitialState::class, game.stateStack.top!!::class)
        assertEquals(1, game.stateStack.size)
        game.stateStack.swap(AnotherState::class)
        assertEquals(AnotherState::class, game.stateStack.top!!::class)
        assertEquals(1, game.stateStack.size)
    }

    @Test
    fun testPushState() {
        game.startGame()
        assertEquals(InitialState::class, game.stateStack.top!!::class)
        assertEquals(1, game.stateStack.size)
        game.stateStack.push(AnotherState::class)
        assertEquals(AnotherState::class, game.stateStack.top!!::class)
        assertEquals(2, game.stateStack.size)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPushUnregisteredGameState() {
        game.startGame()
        game.stateStack.push(OtherGameState::class)
    }

    @Test
    fun testEndGame() {
        game.startGame()
        game.stateStack.push(AnotherState::class)
        assertEquals(2, game.stateStack.size)

        game.endGame()
        assertEquals(0, game.stateStack.size)
    }

    @Test
    fun testEventCalls() {
        val initialState = game.stateStack.getState(InitialState::class) as InitialState
        val anotherState = game.stateStack.getState(AnotherState::class) as AnotherState

        // State Listeners are not registered yet
        assertFalse(initialState.eventCalled)
        assertFalse(anotherState.eventCalled)
        game.plugin.server.pluginManager.callEvent(TestEvent())
        assertFalse(initialState.eventCalled)
        assertFalse(anotherState.eventCalled)

        game.startGame()
        assertEquals(InitialState::class, game.stateStack.top!!::class)
        assertEquals(1, game.stateStack.size)

        // All state Listeners are registered but are only enabled for active states
        // In this case, only InitialState is active
        assertFalse(initialState.eventCalled)
        assertFalse(anotherState.eventCalled)
        game.plugin.server.pluginManager.callEvent(TestEvent())
        assertTrue(initialState.eventCalled)
        assertFalse(anotherState.eventCalled)

        game.stateStack.push(AnotherState::class)
        assertEquals(AnotherState::class, game.stateStack.top!!::class)
        assertEquals(2, game.stateStack.size)

        // InititalState and AnotherState are both active
        assertTrue(initialState.eventCalled)
        assertFalse(anotherState.eventCalled)
        game.plugin.server.pluginManager.callEvent(TestEvent())
        assertTrue(initialState.eventCalled)
        assertTrue(anotherState.eventCalled)

        initialState.eventCalled = false
        anotherState.eventCalled = false

        game.endGame()

        // State Listeners are unregistered when the game ends
        assertFalse(initialState.eventCalled)
        assertFalse(anotherState.eventCalled)
        game.plugin.server.pluginManager.callEvent(TestEvent())
        assertFalse(initialState.eventCalled)
        assertFalse(anotherState.eventCalled)
    }
}