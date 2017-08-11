package kpiston

import kpiston.util.Logging
import com.dumptruckman.bukkit.utils.MockPlayerFactory
import com.dumptruckman.bukkit.utils.SampleGame
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class GamesManagerTest {

    object TestPlugin : com.dumptruckman.bukkit.utils.TestPlugin()

    val plugin = TestPlugin

    @Before
    fun setUp() {
        Logging.init(TestPlugin)
    }

    @Test
    fun testCreateGame() {
        val queue = plugin.gamesManager.getMatchmakingQueue(SampleGame::class)
        assertTrue(queue.isEmpty())
        plugin.gamesManager.createNewGame { SampleGame(plugin) }
        assertEquals(1, queue.size)
        plugin.gamesManager.createNewGame { SampleGame(plugin) }
        assertEquals(2, queue.size)
    }

    @Test
    fun testJoinGame() {
        val matchmaking = plugin.gamesManager.getMatchmakingQueue(SampleGame::class)
        val playing = plugin.gamesManager.getInProgressQueue(SampleGame::class)
        val game1 = plugin.gamesManager.createNewGame { SampleGame(plugin) }
        val game2 = plugin.gamesManager.createNewGame { SampleGame(plugin) }
        assertTrue(game1.players.isEmpty())
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinGameResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test1"), SampleGame::class))
        assertTrue(game1 in matchmaking)
        assertFalse(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(1, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinGameResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test2"), SampleGame::class))
        assertTrue(game1 in matchmaking)
        assertFalse(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(2, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinGameResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test3"), SampleGame::class))
        assertTrue(game1 in matchmaking)
        assertFalse(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(3, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinGameResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test4"), SampleGame::class))
        assertFalse(game1 in matchmaking)
        assertTrue(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(4, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinGameResult.NOT_ACCEPTING_PLAYERS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test5"), game1))
        assertFalse(game1 in matchmaking)
        assertTrue(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(4, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinGameResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test5"), SampleGame::class))
        assertFalse(game1 in matchmaking)
        assertTrue(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(4, game1.players.count)
        assertEquals(1, game2.players.count)
        assertEquals(JoinGameResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test6"), SampleGame::class))
        assertEquals(JoinGameResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test7"), SampleGame::class))
        assertEquals(JoinGameResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test8"), SampleGame::class))
        assertEquals(JoinGameResult.NO_GAME, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test9"), SampleGame::class))
    }
}