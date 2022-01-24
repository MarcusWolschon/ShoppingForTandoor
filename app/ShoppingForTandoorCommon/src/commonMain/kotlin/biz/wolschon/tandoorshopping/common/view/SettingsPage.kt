package biz.wolschon.tandoorshopping.common.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import biz.wolschon.tandoorshopping.common.model.Model

@Composable
fun SettingsPage(model: Model) {
    var baseUrlState by remember { mutableStateOf(model.baseUrl ?: Model.defaultBaseURL) }//model.baseUrlLive.collectAsState(initial = Model.defaultBaseURL)
    val apiTokenState = model.apiTokenLive.collectAsState(initial = "")

    Column {
        Text(
            "Settings",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        TextField(
            value = baseUrlState,
            label = { Text("Server") },
            onValueChange = { value ->
                model.baseUrl = value
                baseUrlState = value
                model.errorMessage.value = null
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
    }
}