package kg.doseeare.controlz_timetravel.core.controller

import kg.doseeare.controlz_timetravel.core.model.StateZ
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * A controller that manages state history and provides time-travel capabilities.
 *
 * It allows stepping backward and forward through recorded states,
 * jumping to a specific index, retrieving the full history, and injecting
 * new states from JSON.
 *
 * @param T The type of state managed by this controller.
 */
internal interface StateController<T> {
	
	/**
	 * Moves one step backward in the state history, if possible.
	 */
	fun stepBack()
	
	/**
	 * Moves one step forward in the state history, if possible.
	 */
	fun stepForward()
	
	/**
	 * Jumps to the state at the given index in the history.
	 *
	 * @param index The target index to jump to.
	 * @return The [StateZ] at the specified index.
	 */
	fun jumpTo(index: Int): StateZ<T>
	
	/**
	 * Returns the full list of recorded states.
	 *
	 * @return A list of [StateZ] representing the state history.
	 */
	fun getHistory(): List<StateZ<T>>
	
	/**
	 * Returns a flow that emits the entire state history whenever it changes.
	 *
	 * @return A [Flow] of lists of [StateZ].
	 */
	fun getHistoryFlow(): Flow<List<StateZ<T>>>
	
	/**
	 * Returns the index of the current state in the history.
	 *
	 * @return The current index.
	 */
	fun currentIndex(): Int
	
	/**
	 * Returns the current state object, or null if none exists.
	 *
	 * @return The current [StateZ] or null.
	 */
	fun currentState(): StateZ<T>?
	
	/**
	 * Injects a new state from a JSON string.
	 *
	 * @param json The JSON representation of the state to inject.
	 * @return A [Result] indicating success or failure.
	 */
	fun injectState(json: String): Result<Unit>
	
	/**
	 * Builder class for constructing a [StateController].
	 *
	 * Requires a [StateFlow], [CoroutineScope], and [StateObserver].
	 */
	class Builder<T> {
		var stateFlow: StateFlow<T>? = null
		var coroutineScope: CoroutineScope? = null
		var stateObserver: StateObserver<T>? = null
		
		/**
		 * Builds a [StateController] instance using the provided parameters.
		 *
		 * @throws IllegalArgumentException if any required parameter is null.
		 */
		fun build(): StateController<T> {
			requireNotNull(stateFlow) { "StateFlow cannot be null" }
			requireNotNull(coroutineScope) { "CoroutineScope cannot be null" }
			requireNotNull(stateObserver) { "StateObserver cannot be null" }
			
			return StateControllerImpl(
				stateFlow = stateFlow!!,
				coroutineScope = coroutineScope!!,
				stateObserver = stateObserver!!
			)
		}
	}
}

/**
 * Observer interface for listening to state changes.
 *
 * @param T The type of state being observed.
 */
interface StateObserver<T> {
	/**
	 * Called whenever the observed state changes.
	 *
	 * @param state The new state value.
	 */
	fun onChanged(state: T)
}
