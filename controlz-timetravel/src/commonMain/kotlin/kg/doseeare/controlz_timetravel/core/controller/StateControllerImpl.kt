package kg.doseeare.controlz_timetravel.core.controller

import kg.doseeare.controlz_timetravel.core.model.StateZ
import kg.doseeare.controlz_timetravel.core.util.JsonUtil
import kg.doseeare.controlz_timetravel.ui.TimeTravelManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class StateControllerImpl<T>(
	private val stateFlow: StateFlow<T>,
	private val stateObserver: StateObserver<T>,
	coroutineScope: CoroutineScope,
) : StateController<T> {
	
	private val history = mutableListOf<StateZ<T>>()
	private var currentIndex: Int = -1
	
	private val _historyFlow = MutableStateFlow<List<StateZ<T>>>(emptyList())
	override fun getHistoryFlow(): Flow<List<StateZ<T>>> = _historyFlow
	
	private var isInternalUpdate = false
	
	init {
		coroutineScope.launch(Dispatchers.Default) {
			stateFlow.collect { newState ->
				if (isInternalUpdate) {
					stateObserver.onChanged(newState)
					isInternalUpdate = false
				} else {
					addState(newState)
				}
			}
		}
	}
	
	override fun getHistory(): List<StateZ<T>> = history.toList()
	
	override fun currentIndex(): Int = currentIndex
	
	override fun currentState(): StateZ<T>? = history.getOrNull(currentIndex)
	
	override fun injectState(json: String): Result<Unit> {
		try {
			val state = JsonUtil.toObject(sampleObject = history.first().state, json)
			if (state != null) {
				addState(state)
				stateObserver.onChanged(state)
			}
			return Result.success(Unit)
		} catch (e: Exception) {
			return Result.failure(e)
		}
	}
	
	override fun jumpTo(index: Int): StateZ<T> {
		val targetIndex = index.coerceIn(0, history.lastIndex)
		currentIndex = targetIndex
		isInternalUpdate = true
		
		history.replaceAllIndexed { idx, s ->
			if (idx == targetIndex) s.copy(isCurrent = true) else s.copy(isCurrent = false)
		}
		_historyFlow.value = history.toList()
		
		stateObserver.onChanged(history[targetIndex].state)
		return history[targetIndex]
	}
	
	override fun stepBack() {
		if (currentIndex > 0) jumpTo(currentIndex - 1)
	}
	
	override fun stepForward() {
		if (currentIndex < history.lastIndex) jumpTo(currentIndex + 1)
	}
	
	private fun addState(state: T) {
		val stateZ = StateZ(
			state = state,
			isCurrent = true,
			diff = DiffFinder.computeJsonDiff(history.lastOrNull()?.state, state),
		)
		if (history.isNotEmpty()) {
			history[history.lastIndex] =
				history.last().copy(isCurrent = false)
		}
		history.add(stateZ)
		currentIndex = history.lastIndex
		_historyFlow.value = history.toList()
		TimeTravelManager.markCurrentController(this@StateControllerImpl)
		
	}
}

private inline fun <T> MutableList<T>.replaceAllIndexed(transform: (Int, T) -> T) {
	for (i in indices) {
		this[i] = transform(i, this[i])
	}
}
