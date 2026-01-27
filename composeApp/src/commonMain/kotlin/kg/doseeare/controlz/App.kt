package kg.doseeare.controlz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kg.doseeare.controlz_timetravel.ui.entry_point.TimeTravelPanel
import kg.doseeare.controlz_timetravel.ui.entry_point.TimeTravelPanelDrawer
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewmodel = viewModel { TestViewModel() }
	    
        val state = viewmodel.state.collectAsState()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
	    
	    TimeTravelPanelDrawer(true) {
	         Column(
	             modifier = Modifier
	                 .background(MaterialTheme.colorScheme.primaryContainer)
	                 .safeContentPadding()
	                 .fillMaxSize()
	                 .padding(horizontal = 16.dp, vertical = 8.dp),
	             horizontalAlignment = Alignment.CenterHorizontally,
	         ) {
	
	             ZButton(modifier = Modifier.fillMaxWidth(), text = "Time Travel", onClick = {
	                 scope.launch {
	                     drawerState.open()
	                 }
	             })
	
	             if (state.value.dictionary.isEmpty()) {
	                 Text(
	                     modifier = Modifier.fillMaxWidth().height(126.dp),
	                     text = "EMPTY 😭😭",
	                     textAlign = TextAlign.Center,
	                     style = TextStyle.Default.copy(fontSize = 24.sp)
	                 )
	             } else {
	                 LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
	                     items(count = state.value.dictionary.size) { index ->
	                         Row(modifier = Modifier.fillMaxWidth()) {
	                             Text(state.value.dictionary[index], modifier = Modifier.padding(8.dp).weight(1f))
	
	                             ZButton(text = "remove", onClick = {
	                                 viewmodel.postAction(TestAction.RemoveDictionary(index))
	                             })
	                         }
	                     }
	                 }
	             }
	
	             val textField = remember { mutableStateOf("") }
	
	
	             Row(modifier = Modifier.fillMaxWidth()) {
	                 ZButton(modifier = Modifier.fillMaxWidth(), text = "Clear List", onClick = {
	                     viewmodel.postAction(TestAction.ClearDictionary)
	                     textField.value = ""
	                 })
	             }
	
	             TextField(
	                 modifier = Modifier.fillMaxWidth(),
	                 value = textField.value,
	                 placeholder = {
	                     Text("Text here")
	                 },
	                 onValueChange = {
	                     textField.value = it
	                 })
	
	             ZButton(modifier = Modifier.fillMaxWidth(), text = "Add Item", onClick = {
	                 viewmodel.postAction(TestAction.PostDictionary(textField.value))
	                 textField.value = ""
	             })
	
	         }
	     }
    }
}


@Composable
private fun ZButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(modifier = modifier, onClick = onClick) {
        Text(text)
    }
}