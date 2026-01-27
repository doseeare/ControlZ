package kg.doseeare.controlz_timetravel.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kg.doseeare.controlz_timetravel.core.model.ChangeType
import kg.doseeare.controlz_timetravel.core.model.StateZ

@Composable
internal fun ItemContent(
	item: StateZ<*>,
	index: Int,
	onClick: () -> Unit,
	onDetailClick: () -> Unit,
) {
	val containerColor =
		if (item.isCurrent) MaterialTheme.colorScheme.primaryContainer
		else MaterialTheme.colorScheme.surface
	
	val containsError = remember { mutableStateOf(false) }
	
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 12.dp, vertical = 6.dp)
			.combinedClickable(
				onClick = onClick,
				onLongClick = {
					if (!containsError.value) {
						onDetailClick()
					}
				}
			),
		colors = CardDefaults.cardColors(containerColor = containerColor),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
	) {
		Column(modifier = Modifier.padding(12.dp)) {
			
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					text = "#$index",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.primary
				)
				
				Spacer(Modifier.width(8.dp))
				
				Text(
					modifier = Modifier.weight(1f),
					text = item.state::class.simpleName.orEmpty(),
					style = MaterialTheme.typography.titleMedium
				)
				
				Text(
					text = item.timestamp,
					style = MaterialTheme.typography.labelSmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				
				IconButton(onClick = { onDetailClick }) {
					Icon(
						modifier = Modifier.clickable(enabled = true, onClick = onDetailClick),
						imageVector = Icons.Default.MoreVert,
						contentDescription = "Details",
					)
				}
			}
			
			item.diff?.forEach { (key, value) ->
				Spacer(Modifier.height(8.dp))
				
				Text(
					text = key,
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.secondary
				)
				
				value.forEach { change ->
					val (color, prefix) = when (change.type) {
						ChangeType.ADDED -> MaterialTheme.colorScheme.tertiary to "+"
						ChangeType.REMOVED -> MaterialTheme.colorScheme.error to "-"
						ChangeType.CHANGED -> MaterialTheme.colorScheme.primary to "•"
						ChangeType.ERROR -> {
							containsError.value = true
							MaterialTheme.colorScheme.error to "\uD83D\uDEA8"
						}
					}
					
					Text(
						modifier = Modifier.padding(start = 12.dp, top = 2.dp),
						text = "$prefix ${change.changes}",
						style = MaterialTheme.typography.bodySmall,
						color = color
					)
				}
			}
		}
	}
}

