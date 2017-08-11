package kpiston.states

import kpiston.Game
import kpiston.GameState

abstract class StartCountdownState<G : Game<G>>(game: G) : GameState<G>(game) {

    override fun onSetUp() {

    }
}