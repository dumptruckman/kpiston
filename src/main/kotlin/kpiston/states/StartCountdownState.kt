package kpiston.states

import kpiston.Game
import kpiston.GameState
import org.bukkit.plugin.Plugin

abstract class StartCountdownState<T : Plugin>(game: Game<T>) : GameState<T>(game) {

    override fun onSetUp() {

    }
}