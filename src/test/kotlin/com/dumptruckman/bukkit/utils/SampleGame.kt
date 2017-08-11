package com.dumptruckman.bukkit.utils

import kpiston.Game
import kpiston.GameState
import kpiston.arena.ArenaWorld
import kpiston.events.MatchmakingCompleteEvent
import kpiston.states.MatchmakingState
import kpiston.states.StartCountdownState
import org.bukkit.event.EventHandler
import kotlin.reflect.KClass

class SampleGame(plugin: TestPlugin) : Game<TestPlugin>(plugin) {

    override val gameStateSuppliers: List<() -> GameState<TestPlugin>> = listOf(
            { Matchmaking(this) },
            { Countdown(this) }
    )

    override val initialState: KClass<out MatchmakingState<TestPlugin>> = Matchmaking::class
    override val arena = ArenaWorld(this, "test1", "test")

    class Matchmaking(game: SampleGame) : MatchmakingState<TestPlugin>(game, 4)
    class Countdown(game: SampleGame) : StartCountdownState<TestPlugin>(game)

    @EventHandler
    fun matchmakingComplete(event: MatchmakingCompleteEvent) {
        if (this ignores event) return
        if (stateStack.top !is MatchmakingState) throw IllegalStateException("MatchmakingCompleteEvent handled when " +
                "game is not in Matchmaking")
        stateStack.swap(Countdown::class)
    }
}