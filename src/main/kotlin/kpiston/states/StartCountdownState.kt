package kpiston.states

import kpiston.Game
import kpiston.GameState
import kpiston.events.CountdownCompleteEvent
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