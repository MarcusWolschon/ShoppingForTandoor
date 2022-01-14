package biz.wolschon.tandoorishopping.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import biz.wolschon.tandoorishopping.common.model.Model
import kotlinx.coroutines.launch

@Composable
fun App(model: Model) {
    var text by remember { mutableStateOf("Perform test request") }
    val apiUrlState = model.apiUrlLive.collectAsState(initial = Model.defaultApiURL)
    val apiTokenState = model.apiTokenLive.collectAsState(initial = Model.defaultApiURL)
    val scope = rememberCoroutineScope()

    Column {
        TextField(
            value = apiUrlState.value,
            label = { Text("Server") },
            onValueChange = { value ->
                model.apiUrl = value
            },
            singleLine = true
        )
        TextField(
            value = apiTokenState.value,
            label = { Text("API Token") },
            onValueChange = { value ->
                model.apiToken = value
            },
            singleLine = true
        )

        Button(onClick = {
            scope.launch(NetworkDispatcher) {
                model.fetchShoppingLists()
                text = model.fetchShoppingLists()
                    ?.let { list ->
                        "${list.size} lists\n${list.filter { !it.finished }.size} not yet finished"
                    }
                    ?: "no shopping lists found"
            }
        }) {
            Text(text)
        }
    }
}
