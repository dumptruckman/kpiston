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
        plugin.gamesManager.createNewGame { manager -> SampleGame(manager) }
        assertEquals(1, queue.size)
        plugin.gamesManager.createNewGame { manager -> SampleGame(manager) }
        assertEquals(2, queue.size)
    }

    @Test
    fun testJoinGame() {
        val matchmaking = plugin.gamesManager.getMatchmakingQueue(SampleGame::class)
        val playing = plugin.gamesManager.getInProgressQueue(SampleGame::class)
        val game1 = plugin.gamesManager.createNewGame { manager -> SampleGame(manager) }
        val game2 = plugin.gamesManager.createNewGame { manager -> SampleGame(manager) }
        assertTrue(game1.players.isEmpty())
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test1"), SampleGame::class))
        assertTrue(game1 in matchmaking)
        assertFalse(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(1, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test2"), SampleGame::class))
        assertTrue(game1 in matchmaking)
        assertFalse(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(2, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test3"), SampleGame::class))
        assertTrue(game1 in matchmaking)
        assertFalse(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(3, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test4"), SampleGame::class))
        assertFalse(game1 in matchmaking)
        assertTrue(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(4, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinResult.NOT_ACCEPTING_PLAYERS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test5"), game1))
        assertFalse(game1 in matchmaking)
        assertTrue(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(4, game1.players.count)
        assertTrue(game2.players.isEmpty())
        assertEquals(JoinResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test5"), SampleGame::class))
        assertFalse(game1 in matchmaking)
        assertTrue(game1 in playing)
        assertTrue(game2 in matchmaking)
        assertFalse(game2 in playing)
        assertEquals(4, game1.players.count)
        assertEquals(1, game2.players.count)
        assertEquals(JoinResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test6"), SampleGame::class))
        assertEquals(JoinResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test7"), SampleGame::class))
        assertEquals(JoinResult.SUCCESS, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test8"), SampleGame::class))
        assertEquals(JoinResult.NO_GAME, plugin.gamesManager.requestJoin(MockPlayerFactory.createMockedPlayer("test9"), SampleGame::class))
    }
}