package kg.doseeare.controlz_timetravel.ui.entry_point

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable

@Composable
fun TimeTravelPanelDrawer(enabled: Boolean, content: @Composable () -> Unit) {
	val drawerState = rememberDrawerState(DrawerValue.Closed)
	
	ModalNavigationDrawer(
		drawerState = drawerState, drawerContent = {
			if (enabled) TimeTravelPanel()
		}, content = content
	)
}