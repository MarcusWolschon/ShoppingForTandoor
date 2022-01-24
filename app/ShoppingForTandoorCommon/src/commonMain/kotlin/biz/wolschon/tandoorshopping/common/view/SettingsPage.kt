package biz.wolschon.tandoorshopping.common.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.model.SettingsModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SettingsPage(settings: SettingsModel, errorMessage: MutableStateFlow<String?>) {
    var baseUrlState by remember { mutableStateOf(settings.baseUrl ?: Model.defaultBaseURL) }//model.baseUrlLive.collectAsState(initial = Model.defaultBaseURL)
    val apiTokenState = settings.apiTokenLive.collectAsState(initial = "")

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
                settings.baseUrl = value
                baseUrlState = value
                errorMessage.value = null
            },
            singleLine = true
        )
        TextField(
            value = apiTokenState.value,
            label = { Text("API Token") },
            onValueChange = { value ->
                settings.apiToken = value
            },
            singleLine = true
        )
    }
}