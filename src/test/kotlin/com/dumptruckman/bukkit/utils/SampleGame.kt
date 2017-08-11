package com.dumptruckman.bukkit.utils

import kpiston.Game
import kpiston.GameState
import kpiston.GamesManager
import kpiston.arena.ArenaWorld
import kpiston.events.MatchmakingCompleteEvent
import kpiston.states.MatchmakingState
import kpiston.states.StartCountdownState
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
    class Countdown(game: SampleGame) : StartCountdownState<SampleGame>(game)

    @EventHandler
    fun matchmakingComplete(event: MatchmakingCompleteEvent<SampleGame>) {
        if (this ignores event) return
        if (stateStack.top !is MatchmakingState<*>) throw IllegalStateException("MatchmakingCompleteEvent handled when " +
                "game is not in Matchmaking")
        stateStack.swap(Countdown::class)
    }
}