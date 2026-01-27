package kg.doseeare.controlz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kg.doseeare.controlz_timetravel.TimeTravelRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : State, A : Action>(defaultState: S) : ViewModel() {
	
	val state: StateFlow<S>
		get() = _state.asStateFlow()
	
	private val _state: MutableStateFlow<S> = MutableStateFlow(
		defaultState
	)
	
	protected fun updateState(block: (S) -> S) {
		viewModelScope.launch {
			_state.emit(block.invoke(_state.value))
		}
	}
	
	abstract fun postAction(action: A)
	
	init {
		TimeTravelRegistry.register(
			scope = viewModelScope,
			stateFlow = state,
			enabled = true, //BuildConfig.Debug
			onChanged = { travelState ->
				updateState { travelState }
			})
	}
}

interface Action

interface State