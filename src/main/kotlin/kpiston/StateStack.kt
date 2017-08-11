package kpiston

import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.reflect.KClass

class StateStack<T : Plugin>(val game: Game<T>, private val gameStateSuppliers: List<() -> GameState<T>>) {

    private val stateStack = ArrayDeque<GameState<T>>()

    private val gameStates: Map<KClass<out GameState<T>>, GameState<T>> by lazy {
        val result = mutableMapOf<KClass<out GameState<T>>, GameState<T>>()
        gameStateSuppliers.forEach {
            val gameState = it.invoke()
            result[gameState::class] = gameState
        }
        result
    }

    /**
     * Retrieves this game's copy of the state object for the given [GameState] class.
     */
    fun getState(stateType: KClass<out GameState<*>>): GameState<T> {
        return gameStates[stateType] ?: throw IllegalArgumentException("$stateType is not a valid state for this game")
    }

    /**
     * Pushes a game state onto the top of the state stack.
     *
     * The [GameState] object will be resumed when pushed onto the stack.
     *
     * @param stateType This class is used as a key to retrieve the [GameState] instance this game uses.
     */
    fun push(stateType: KClass<out GameState<T>>) {
        val state = getState(stateType)
        state.resume()
        stateStack.push(state)
    }

    /**
     * Pops the top game state off of the game state stack.
     *
     * The [GameState] object will be paused when popped from the stack.
     *
     * @throws NoSuchElementException if the game state stack is empty.
     */
    fun pop(): GameState<T> {
        val state = stateStack.pop()
        state.pause()
        return state
    }

    /**
     * Pops the top game state off of the game state stack and pushes the new state onto the top of the stack.
     *
     * The [GameState] that is removed will be paused and the one added will be resumed.
     *
     * @param stateType This class is used as a key to retrieve the [GameState] instance to push onto the stack.
     */
    fun swap(stateType: KClass<out GameState<T>>) {
        pop()
        push(stateType)
    }

    /**
     * Returns the active state at the top of the state stack.
     */
    val top: GameState<T>?
        get() = stateStack.peek()

    fun isNotEmpty() = stateStack.isNotEmpty()

    val size: Int
        get() = stateStack.size

    val activeStates: Array<GameState<T>>
        get() = stateStack.toTypedArray()

    val allStates: Iterator<GameState<T>>
        get() = gameStates.values.iterator()
}