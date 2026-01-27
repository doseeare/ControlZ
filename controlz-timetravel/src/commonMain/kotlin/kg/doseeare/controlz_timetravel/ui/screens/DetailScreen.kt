package kg.doseeare.controlz_timetravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.sebastianneubauer.jsontree.JsonTree
import com.sebastianneubauer.jsontree.TreeState
import com.sebastianneubauer.jsontree.search.rememberSearchState
import kg.doseeare.controlz_timetravel.core.model.StateZ
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailScreen(
	state: StateZ<*>,
	onBack: () -> Unit,
) {
	val clipboard = LocalClipboardManager.current
	val scope = rememberCoroutineScope()
	val stateJson = remember { state.toJson() }
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("State details") },
				navigationIcon = {
					IconButton(onClick = onBack) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
					}
				}
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				onClick = {
					scope.launch {
						clipboard.setText(buildAnnotatedString { append(stateJson) })
					}
				}
			) {
				Icon(Icons.Filled.ContentCopy, null)
			}
		}
	) { padding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(padding)
		) {
			val jsonSearcher = rememberSearchState()
			
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				shape = RoundedCornerShape(16.dp),
				elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
				colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
			) {
				Column(
					modifier = Modifier
						.fillMaxWidth()
				) {
					TextField(
						modifier = Modifier.fillMaxWidth(),
						value = jsonSearcher.query.orEmpty(),
						placeholder = { Text("Search in the state") },
						onValueChange = { jsonSearcher.query = it },
						singleLine = true,
						shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
						colors = TextFieldDefaults.colors()
					)
					
					JsonTree(
						modifier = Modifier
							.fillMaxWidth()
							.background(
								Color.White,
								RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
							)
							.padding(12.dp),
						json = stateJson,
						onLoading = { CircularProgressIndicator() },
						initialState = TreeState.FIRST_ITEM_EXPANDED,
						iconSize = 20.dp,
						showIndices = false,
						showItemCount = true,
						expandSingleChildren = true,
						searchState = jsonSearcher,
						lazyListState = rememberLazyListState(),
					)
				}
			}
		}
		
	}
}
