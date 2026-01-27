package kg.doseeare.controlz_timetravel.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kg.doseeare.controlz_timetravel.core.model.StateZ

@Composable
internal fun ListScreen(
	history: List<StateZ<*>>,
	onItemClicked: (index: Int) -> Unit,
	onItemLongClicked: (index: Int) -> Unit,
	onBack: () -> Unit,
	onForward: () -> Unit,
	onInjectState : () -> Unit,
) {
	val lazyState = rememberLazyListState()
	
	Scaffold(
		bottomBar = {
			NavigationBar {
				NavigationBarItem(
					selected = false,
					onClick = onBack,
					icon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) },
					label = { Text("Back") }
				)
				NavigationBarItem(
					selected = false,
					onClick = onInjectState,
					icon = { Icon(Icons.AutoMirrored.Filled.NoteAdd, null) },
					label = { Text("Inject state") }
				)
				NavigationBarItem(
					selected = false,
					onClick = onForward,
					icon = { Icon(Icons.AutoMirrored.Filled.ArrowForward, null) },
					label = { Text("Forward") }
				)
			}
		},
		floatingActionButton = {
			FloatingActionButton(
				onClick = {
					if (history.isNotEmpty()) {
						lazyState.requestScrollToItem(history.lastIndex)
					}
				}
			) {
				Icon(Icons.Filled.ArrowDownward, contentDescription = "Scroll down")
			}
		}
	) { padding ->
		
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.padding(padding),
			state = lazyState,
			contentPadding = PaddingValues(vertical = 8.dp)
		) {
			items(
				count = history.size,
				key = { it.hashCode() }
			) { index ->
				val item = history[index]
				ItemContent(
					item = item,
					index = index,
					onClick = { onItemClicked(index) },
					onDetailClick = { onItemLongClicked(index) }
				)
			}
		}
	}
}
