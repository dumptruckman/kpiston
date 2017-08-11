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
import kpiston.event.CountdownCompleteEvent
import kpiston.extensions.callEvent
import kpiston.extensions.isRunning
import kpiston.tasks.CountdownTask

/**
 * The game state that follows [MatchmakingState] for most games. It signifies that enough players have joined the game
 * and the countdown to begin actual game play is happening.
 */
abstract class StartCountdownState<G : Game<G>>(game: G, countdownLength: Int) : GameState<G>(game) {

    val countdownTask = CountdownTask(countdownLength, { remaining -> secondsChange(remaining) },
            { countdownComplete() })

    override fun onResume() {
        if (!countdownTask.isRunning()) {
            countdownTask.runTaskTimer(game.plugin, 0, 1)
        }
    }

    private fun secondsChange(remainingSeconds: Int) {

    }

    private fun countdownComplete() {
        CountdownCompleteEvent(game).callEvent()
    }
}