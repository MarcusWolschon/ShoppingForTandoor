package biz.wolschon.tandoorishopping.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry
import biz.wolschon.tandoorishopping.common.model.Model
import biz.wolschon.tandoorishopping.common.view.SettingsPage
import biz.wolschon.tandoorishopping.common.view.shoppingListList
import biz.wolschon.tandoorishopping.common.view.shoppingListView
import kotlinx.coroutines.launch

private enum class Pages { LISTS, LIST, SETTINGS }

@Composable
fun App(model: Model) {
    // state
    var pageToShow by remember { mutableStateOf(Pages.LISTS) }
    var showFinished by remember { mutableStateOf(false) }
    var showChecked by remember { mutableStateOf(false) }
    var currentShoppingList by remember { mutableStateOf<TandoorShoppingList?>(null) }
    var allShoppingLists by remember { mutableStateOf<List<TandoorShoppingList>?>(null) }
    val scope = rememberCoroutineScope()

    // pages

    if (model.settingsIncomplete) {
        pageToShow = Pages.SETTINGS
    } else if (allShoppingLists == null) {
        scope.launch(NetworkDispatcher) {
            model.fetchShoppingLists()?.let { list -> allShoppingLists = list }
        }
    }
    if (pageToShow == Pages.LIST && currentShoppingList == null) {
        pageToShow = Pages.LISTS
    }

    fun updateFoodEntry(foodItem: TandoorShoppingListEntry, checked: Boolean) {
        scope.launch(NetworkDispatcher) {
            model.updateShoppingListItemChecked(foodItem.id, checked)
            //model.updateShoppingListItemChecked(foodItem.copy(checked = checked))
            model.fetchShoppingLists()?.let { list ->
                allShoppingLists = list
                val oldListId = currentShoppingList?.id
                currentShoppingList = list.find { sl -> sl.id == oldListId }
            }
        }
    }

    Column {
        // top level bar
        Row {
            Button(
                enabled = !model.settingsIncomplete,
                onClick = {
                    scope.launch(NetworkDispatcher) {
                        model.fetchShoppingLists()?.let { list -> allShoppingLists = list }
                    }
                }) {
                Text("\uD83D\uDDD8")
            }

            Button(
                enabled = pageToShow != Pages.LISTS && allShoppingLists != null,
                onClick = { pageToShow = Pages.LISTS }) {
                Text("shopping lists")
            }

            Spacer(Modifier.fillMaxWidth().weight(2f))

            Button(
                //always enabled //enabled = pageToShow != Pages.SETTINGS,
                onClick = { pageToShow = if (pageToShow == Pages.SETTINGS) Pages.LISTS else Pages.SETTINGS },
                modifier = Modifier.weight(1f)
            ) {
                Text("⚙")
            }
        }

        when (pageToShow) {
            Pages.SETTINGS -> SettingsPage(model)

            Pages.LISTS -> allShoppingLists?.let {
                Text(
                    "All shopping lists:",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Row {
                    Checkbox(checked = showFinished, onCheckedChange = { checked -> showFinished = checked })
                    Text("show finished lists", Modifier.align(CenterVertically))
                }
                shoppingListList(it, showFinished) { shoppingList ->
                    currentShoppingList = shoppingList
                    pageToShow = Pages.LIST
                }
            }

            Pages.LIST -> currentShoppingList?.let {
                Text(
                    "Shopping list ${it.id} ${it.note ?: ""}:",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Row {
                    Checkbox(checked = showChecked, onCheckedChange = { checked -> showChecked = checked })
                    Text("show checked foods", Modifier.align(CenterVertically))
                }
                shoppingListView(it, showChecked) { foodItem, checked -> updateFoodEntry(foodItem, checked) }
            }
        }
    }
}


