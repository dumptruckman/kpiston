package kpiston.events

import kpiston.Game
import org.bukkit.event.Event

abstract class GameEvent(val game: Game<*>) : Event() {

    /**
     * Indicates whether the given game owns (initiated) this event.
     *
     * A game and its states should only care about events it (or its states) initiated.
     *
     * See [Game.ignores].
     */
    fun isOwnedBy(game: Game<*>) = this.game === game
    /**
     * Indicates whether the given game does not own (initiated) this event.
     *
     * A game and its states should only care about events it (or its states) initiated.
     *
     * See [Game.ignores].
     */
    fun isNotOwnedBy(game: Game<*>) = this.game !== game

    /**
     * Set this to true when handled by an EventHandler to indicate to the player list that the event has been isHandled
     * and may proceed with adding the player to the game if the event is not also cancelled.
     */
    var isHandled = false
}