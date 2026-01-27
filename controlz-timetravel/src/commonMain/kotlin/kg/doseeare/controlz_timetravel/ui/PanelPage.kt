package kg.doseeare.controlz_timetravel.ui

import kg.doseeare.controlz_timetravel.core.model.StateZ

internal sealed interface PanelPage {
    object ListScreen : PanelPage
    data class DetailScreen(val stateZ: StateZ<*>) : PanelPage
	object InjectScreen : PanelPage
}