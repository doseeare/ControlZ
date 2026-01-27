package kg.doseeare.controlz_timetravel

import kg.doseeare.controlz_timetravel.core.controller.StateController
import kg.doseeare.controlz_timetravel.core.controller.StateObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow


/**
 * Registry for enabling time-travel debugging of state flows.
 *
 * This object provides a simple entry point to register a [StateFlow] with a [StateController],
 * allowing inspection of state history, diffs, and navigation (stepBack, stepForward, jumpTo).
 *
 * Usage:
 * ```
 * TimeTravelRegistry.register(
 *     scope = viewModelScope,
 *     stateFlow = uiState,
 *     enabled = BuildConfig.DEBUG,
 * ) { newState ->
 *     // React to state changes if needed
 * }
 * ```
 */
object TimeTravelRegistry {
	
	/**
	 * Registers a [StateFlow] with the time-travel system.
	 *
	 * @param scope Coroutine scope in which collection of the [stateFlow] will run.
	 *              Typically this is a [ViewModel] scope or lifecycle-aware scope.
	 * @param stateFlow The [StateFlow] representing your UI or domain state.
	 * @param enabled Flag to enable or disable registration. If `false`, the method returns immediately.
	 * @param onChanged Callback invoked whenever the state changes. Useful for logging or side-effects.
	 *
	 */
	fun <T> register(
		scope: CoroutineScope,
		stateFlow: StateFlow<T>,
		enabled: Boolean,
		onChanged: (T) -> Unit,
	) {
		if (!enabled) return
		
		StateController.Builder<T>().apply {
			this.coroutineScope = scope
			this.stateFlow = stateFlow
			this.stateObserver = object : StateObserver<T> {
				override fun onChanged(state: T) {
					onChanged(state)
				}
			}
		}.build()
	}
}
