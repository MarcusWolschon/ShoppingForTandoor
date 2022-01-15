package biz.wolschon.tandoorishopping.common.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import biz.wolschon.tandoorishopping.common.model.Model

@Composable
fun SettingsPage(model: Model) {
    val apiUrlState = model.apiUrlLive.collectAsState(initial = Model.defaultApiURL)
    val apiTokenState = model.apiTokenLive.collectAsState(initial = Model.defaultApiURL)

    Column {
        Text(
            "Settings",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
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
    }
}