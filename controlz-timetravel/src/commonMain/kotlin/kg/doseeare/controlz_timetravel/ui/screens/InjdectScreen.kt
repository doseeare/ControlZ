package kg.doseeare.controlz_timetravel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kg.doseeare.controlz_timetravel.core.controller.StateController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun InjectScreen(
	stateController: StateController<*>?,
	onBack: () -> Unit,
) {
	val newJson = remember { mutableStateOf("") }
	val resultText = remember { mutableStateOf<String?>(null) }
	val isSuccess = remember { mutableStateOf(false) }
	val scope = rememberCoroutineScope()
	val clipboardManager = LocalClipboardManager.current
	
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(Color(0x99000000)),
		contentAlignment = Alignment.Center
	) {
		Surface(
			shape = RoundedCornerShape(20.dp),
			color = Color.White,
			tonalElevation = 8.dp,
			modifier = Modifier
				.fillMaxWidth(0.9f)
				.wrapContentHeight()
		) {
			Column(
				modifier = Modifier.padding(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Text(
					"Inject State",
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold
				)
				
				
				TextField(
					modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 250.dp),
					value = newJson.value,
					placeholder = { Text("Paste JSON state here") },
					onValueChange = { newJson.value = it },
					singleLine = false,
					shape = RoundedCornerShape(12.dp),
					trailingIcon = {
						IconButton(onClick = {
							val clipText = clipboardManager.getText()?.text
							if (!clipText.isNullOrEmpty()) {
								newJson.value = clipText
							}
						}) {
							Icon(Icons.Default.ContentPaste, contentDescription = "Paste")
						}
					},
					colors = TextFieldDefaults.colors(
						focusedContainerColor = Color(0xFFF9F9F9),
						unfocusedContainerColor = Color(0xFFF9F9F9),
						focusedIndicatorColor = Color.Transparent,
						unfocusedIndicatorColor = Color.Transparent
					)
				)
				
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceEvenly
				) {
					Button(
						onClick = { onBack() },
						colors = ButtonDefaults.buttonColors(
							containerColor = Color.LightGray
						),
						shape = RoundedCornerShape(12.dp)
					) {
						Text("Cancel")
					}
					
					Button(
						onClick = {
							stateController?.injectState(newJson.value)
								?.onSuccess {
									resultText.value = "✅ State added successfully"
									isSuccess.value = true
									scope.launch {
										delay(1500)
										onBack()
									}
								}?.onFailure {
									resultText.value = "❌ Error: ${it.message}"
									isSuccess.value = false
								}
						},
						colors = ButtonDefaults.buttonColors(
							containerColor = Color(0xFF4CAF50)
						),
						shape = RoundedCornerShape(12.dp)
					) {
						Text("Add State", color = Color.White)
					}
				}
				
				resultText.value?.let { msg ->
					Text(
						text = msg,
						color = if (isSuccess.value) Color(0xFF2E7D32) else Color(0xFFD32F2F),
						style = MaterialTheme.typography.bodyLarge,
						modifier = Modifier
							.fillMaxWidth()
							.background(
								if (isSuccess.value) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
								RoundedCornerShape(8.dp)
							)
							.padding(12.dp)
					)
				}
			}
		}
	}
}

