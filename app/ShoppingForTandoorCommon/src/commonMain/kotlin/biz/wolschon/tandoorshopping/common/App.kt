package biz.wolschon.tandoorshopping.common

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import biz.wolschon.tandoorshopping.common.api.model.TandoorFood
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingList
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.view.*
import io.ktor.client.features.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.nio.channels.UnresolvedAddressException

private enum class Pages { LISTS, LIST, FOODS, FOOD, SETTINGS }

@Composable
fun App(model: Model) {
    // state
    val errorMessage = model.errorMessage.collectAsState()
    var pageToShow by remember { mutableStateOf(Pages.LISTS) }
    var showFinished by remember { mutableStateOf(false) }
    var showChecked by remember { mutableStateOf(false) }
    var currentShoppingList by remember { mutableStateOf<TandoorShoppingList?>(null) }
    var currentFood by remember { mutableStateOf<TandoorFood?>(null) }
    var allShoppingLists by remember { mutableStateOf<List<TandoorShoppingList>?>(null) }
    var allFoods by remember { mutableStateOf<List<TandoorFood>?>(null) }
    val scope = rememberCoroutineScope()

    // pages
    Log.d("App", "App recomposing pageToShow=$pageToShow")

    val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("App", "NetworkDispatcher got", throwable)
        if (throwable is UnresolvedAddressException) {
            model.apiUrl = null
        }
        if (throwable is ClientRequestException) {
            model.apiToken = null
        }
    }

    val refresh = {
        scope.launch(NetworkDispatcher + errorHandler) {
            model.fetchShoppingLists()?.let { list -> allShoppingLists = list }
            model.fetchFoods()?.let { list -> allFoods = list }
        }
    }

    if (model.settingsIncomplete) {
        pageToShow = Pages.SETTINGS
    } else if (allShoppingLists == null && errorMessage.value == null) {
        refresh.invoke()
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
                onClick = { refresh.invoke() }) {
                Text("\uD83D\uDDD8")
            }

            Button(
                enabled = pageToShow != Pages.LISTS && allShoppingLists != null,
                onClick = { pageToShow = Pages.LISTS }) {
                Text("shopping lists")
            }

            Button(
                enabled = pageToShow != Pages.FOODS && allFoods != null,
                onClick = { pageToShow = Pages.FOODS }) {
                Text("foods")
            }

            Spacer(Modifier.fillMaxWidth().weight(2f))

            Button(
                //always enabled //enabled = pageToShow != Pages.SETTINGS,
                onClick = {

                    Log.d("App", "settings clicked")
                    pageToShow = if (pageToShow == Pages.SETTINGS) Pages.LISTS else Pages.SETTINGS
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("⚙")
            }
        }

        errorMessage.value?.let { error ->
            Row(Modifier.background(Color.Red).fillMaxWidth()) {
                Text(text = error, color = Color.White)
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
                shoppingListView(
                    it,
                    showChecked,
                    onFoodCheckedChanged =  { foodItem, checked -> updateFoodEntry(foodItem, checked) },
                    onFoodSelected = { food ->
                        currentFood = food
                        pageToShow = Pages.FOOD
                    }
                )
            }

            Pages.FOODS -> allFoods?.let {
                Text(
                    "All foods:",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                foodListView(it, showFinished) { food ->
                    currentFood = food
                    pageToShow = Pages.FOOD
                }
            }

            Pages.FOOD -> currentFood?.let {
                Text(
                    "selected food:",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                foodDetailsView(it)
            }
        }
    }
}


