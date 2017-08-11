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
package com.dumptruckman.bukkit.utils

import kpiston.Game
import kpiston.state.GameState
import kpiston.GamesManager
import kpiston.arena.ArenaWorld
import kpiston.event.MatchmakingCompleteEvent
import kpiston.state.MatchmakingState
import kpiston.state.StartCountdownState
import org.bukkit.event.EventHandler
import kotlin.reflect.KClass

class SampleGame(manager: GamesManager<*>) : Game<SampleGame>(manager) {

    override val gameStateSuppliers: List<() -> GameState<SampleGame>> = listOf(
            { Matchmaking(this) },
            { Countdown(this) }
    )

    override val initialState: KClass<out MatchmakingState<SampleGame>> = Matchmaking::class
    override val arena = ArenaWorld(this, "test1", "test")

    class Matchmaking(game: SampleGame) : MatchmakingState<SampleGame>(game, 4)
    class Countdown(game: SampleGame) : StartCountdownState<SampleGame>(game, 10)

    @EventHandler
    fun matchmakingComplete(event: MatchmakingCompleteEvent<SampleGame>) {
        if (this ignores event) return
        if (stateStack.top !is MatchmakingState<*>) throw IllegalStateException("MatchmakingCompleteEvent handled when " +
                "game is not in Matchmaking")
        stateStack.swap(Countdown::class)
    }
}