package kg.doseeare.controlz_timetravel.ui.entry_point

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kg.doseeare.controlz_timetravel.ui.TimeTravelManager
import kg.doseeare.controlz_timetravel.ui.PanelPage
import kg.doseeare.controlz_timetravel.ui.PanelPage.*
import kg.doseeare.controlz_timetravel.ui.screens.DetailScreen
import kg.doseeare.controlz_timetravel.ui.screens.InjectScreen
import kg.doseeare.controlz_timetravel.ui.screens.ListScreen

@Composable
fun TimeTravelPanel() {
	val stateController = TimeTravelManager.getControllerState().collectAsState()
	val currentPanelPage = remember { mutableStateOf<PanelPage>(PanelPage.ListScreen) }
	
	val historyFlow = stateController.value?.getHistoryFlow()?.collectAsState(initial = emptyList())
	
	Crossfade(modifier = Modifier.fillMaxSize(), targetState = currentPanelPage.value) {
		when (it) {
			PanelPage.ListScreen -> ListScreen(
				history = historyFlow?.value.orEmpty(),
				onItemClicked = {
					stateController.value?.jumpTo(it)
				},
				onItemLongClicked = {
					val item = historyFlow?.value?.getOrNull(it)
					if (item == null) return@ListScreen
					currentPanelPage.value = DetailScreen(item)
				},
				onBack = {
					stateController.value?.stepBack()
				},
				onForward = {
					stateController.value?.stepForward()
				},
				onInjectState = {
					currentPanelPage.value = PanelPage.InjectScreen
				})
			
			is PanelPage.DetailScreen -> DetailScreen(it.stateZ) {
				currentPanelPage.value = PanelPage.ListScreen
			}
			
			PanelPage.InjectScreen -> InjectScreen(stateController.value) {
				currentPanelPage.value = PanelPage.ListScreen
			}
		}
		
	}
}

