package kpiston

import kpiston.arena.ArenaWorld
import kpiston.states.MatchmakingState
import kpiston.util.Logging
import com.dumptruckman.bukkit.utils.TestEvent
import org.bukkit.event.EventHandler
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import kotlin.reflect.KClass

class StateStackTest {

    class InitialState(game: Game<TestPlugin>) : MatchmakingState<TestPlugin>(game, 12) {
        var eventCalled = false

        @EventHandler
        fun testEvent(event: TestEvent) {
            eventCalled = true
        }
    }
    class AnotherState(game: Game<TestPlugin>) : GameState<TestPlugin>(game) {
        var eventCalled = false

        @EventHandler
        fun testEvent(event: TestEvent) {
            eventCalled = true
        }
    }
    class OtherGameState(game: Game<TestPlugin>) : GameState<TestPlugin>(game)

    class TestGame(plugin: TestPlugin) : Game<TestPlugin>(plugin) {
        override val gameStateSuppliers: List<() -> GameState<TestPlugin>> = listOf(
                { InitialState(this) },
                { AnotherState(this) }
        )
        override val initialState: KClass<out MatchmakingState<TestPlugin>> = InitialState::class
        override val arena = ArenaWorld(this, "test1", "test")
    }

    object TestPlugin : com.dumptruckman.bukkit.utils.TestPlugin()

    var game: TestGame = TestGame(TestPlugin)

    @Before
    fun setUp() {
        Logging.init(TestPlugin)
        game = TestGame(TestPlugin)
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