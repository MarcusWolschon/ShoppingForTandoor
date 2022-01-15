package biz.wolschon.tandoorishopping.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList
import biz.wolschon.tandoorishopping.common.model.Model
import biz.wolschon.tandoorishopping.common.view.shoppingListList
import biz.wolschon.tandoorishopping.common.view.shoppingListView
import kotlinx.coroutines.launch

@Composable
fun App(model: Model) {
    var showFinished by remember { mutableStateOf(false) }
    var showChecked by remember { mutableStateOf(false) }
    var currentShoppingList by remember { mutableStateOf<TandoorShoppingList?>(null) }
    var allShoppingLists by remember { mutableStateOf<List<TandoorShoppingList>?>(null) }
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
                model.fetchShoppingLists()?.let { list -> allShoppingLists = list }
            }
        }) {
            Text("fetch shopping lists")
        }

        allShoppingLists?.let {
            Row {
                Checkbox(checked = showFinished, onCheckedChange = {checked -> showFinished = checked})
                Text("show finished lists", Modifier.align(CenterVertically))
            }
            shoppingListList(it, showFinished) { shoppingList -> currentShoppingList = shoppingList}
        }

        currentShoppingList?.let {
            Row {
                Checkbox(checked = showChecked, onCheckedChange = {checked -> showChecked = checked})
                Text("show checked foodss", Modifier.align(CenterVertically))
            }
            shoppingListView(it, showFinished)
        }
    }
}
