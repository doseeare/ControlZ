package kg.doseeare.controlz_timetravel.ui

import kg.doseeare.controlz_timetravel.core.controller.StateController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal object TimeTravelManager {
	
	private val stateController: MutableStateFlow<StateController<*>?> =
		MutableStateFlow(null)
	
	fun getControllerState(): StateFlow<StateController<*>?> {
		return stateController.asStateFlow()
	}
	
	fun markCurrentController(stateController: StateController<*>) {
		if (this.stateController.value != stateController) {
			this.stateController.tryEmit(stateController)
		}
	}
}