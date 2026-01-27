package kg.doseeare.controlz

import kotlinx.serialization.Serializable

class TestViewModel() : BaseViewModel<TestState, TestAction>(
	defaultState = TestState(emptyList())
) {
	
	override fun postAction(action: TestAction) {
		when (action) {
			TestAction.ClearDictionary -> clearDictionary()
			is TestAction.PostDictionary -> postDictionary(action.text)
			is TestAction.RemoveDictionary -> removeDictionary(action.index)
		}
	}
	
	private fun postDictionary(text: String) {
		updateState { state ->
			state.copy(
				state.dictionary.toMutableList().apply { this.add(text) }
			)
		}
	}
	
	private fun clearDictionary() {
		updateState { state ->
			state.copy(emptyList())
		}
	}
	
	private fun removeDictionary(index: Int) {
		updateState { state ->
			state.copy(
				state.dictionary.toMutableList().apply { this.removeAt(index) }
			)
		}
	}
	
}

@Serializable
data class TestState(
	val dictionary: List<String>,
	val name: String = "Jason",
) : State

sealed interface TestAction : Action {
	data class PostDictionary(val text: String) : TestAction
	data object ClearDictionary : TestAction
	data class RemoveDictionary(val index: Int) : TestAction
}