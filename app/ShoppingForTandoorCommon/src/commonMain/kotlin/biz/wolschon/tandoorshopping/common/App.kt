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
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.view.*
import io.ktor.client.features.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.nio.channels.UnresolvedAddressException

private enum class Pages { LIST, FOODS, FOOD, SETTINGS }

@Composable
fun App(model: Model) {
    // state
    val platformContext = getPlatformContext()
    val errorMessage = model.errorMessage.collectAsState()
    var pageToShow by remember { mutableStateOf(Pages.LIST) }
    var showChecked by remember { mutableStateOf(false) }
    var currentFood by remember { mutableStateOf<TandoorFood?>(null) }
    var shoppingList by remember { mutableStateOf<List<TandoorShoppingListEntry>?>(null) }
    var allFoods by remember { mutableStateOf<List<TandoorFood>?>(null) }
    val scope = rememberCoroutineScope()

    // pages
    Log.d("App", "App recomposing pageToShow=$pageToShow")

    val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("App", "NetworkDispatcher got", throwable)
        //if (throwable is UnresolvedAddressException) {
            //model.baseUrl = null
        //}
        if (throwable is ClientRequestException) {
            model.apiToken = null
        }
    }

    val refresh = {
        scope.launch(NetworkDispatcher + errorHandler) {
            Log.i("App", "refresh starting")
            model.fetchShoppingList()?.let { entries ->
                Log.i("App", "refresh - fetchShoppingList done")
                shoppingList = entries
            }
            if (model.errorMessage.value == null) {
                Log.i("App", "refresh - fetchFoods starting")
                model.fetchFoods()?.let { list ->
                    Log.i("App", "refresh - fetchFoods done")
                    allFoods = list
                }
                Log.i("App", "refresh done")
            }
        }
    }

    if (model.settingsIncomplete) {
        Log.i("App", "settings incomplete, forcing settings page")
        pageToShow = Pages.SETTINGS
    } else if (shoppingList == null && errorMessage.value == null) {
        if (model.errorMessage.value == null) {
            Log.i("App", "initial refresh")
            refresh.invoke()
        }
    }

    fun updateFoodEntry(foodItem: TandoorShoppingListEntry, checked: Boolean) {
        scope.launch(NetworkDispatcher) {
            model.updateShoppingListItemChecked(foodItem.id, checked)
            //model.updateShoppingListItemChecked(foodItem.copy(checked = checked))
            model.fetchShoppingList()?.let { entries ->
                shoppingList = entries
            }
        }
    }

    Column {
        // top level bar
        Row {
            Button(
                enabled = !model.settingsIncomplete,
                onClick = {
                    Log.i("App", "[refresh] tapped")
                    refresh.invoke()
                }) {
                Text("\uD83D\uDDD8")
            }

            Button(
                enabled = pageToShow != Pages.LIST && shoppingList != null,
                onClick = {
                    Log.i("App", "[list] tapped")
                    pageToShow = Pages.LIST
                }) {
                Text("shopping lists")
            }

            Button(
                enabled = pageToShow != Pages.FOODS && allFoods != null,
                onClick = {
                    Log.i("App", "[foods] tapped")
                    pageToShow = Pages.FOODS
                }) {
                Text("foods")
            }

            Spacer(Modifier.fillMaxWidth().weight(2f))

            Button(
                //always enabled //enabled = pageToShow != Pages.SETTINGS,
                onClick = {
                    Log.i("App", "[settings] tapped")
                    pageToShow = if (pageToShow == Pages.SETTINGS) Pages.LIST else Pages.SETTINGS
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("⚙")
            }
        }

        errorMessage.value?.let { error ->
            Row(Modifier.background(Color.Red).fillMaxWidth()) {
                Text(text = error, color = Color.White, maxLines = 3)
            }
        }

        when (pageToShow) {
            Pages.SETTINGS -> SettingsPage(model)

            Pages.LIST -> shoppingList?.let {
                Text(
                    "Shopping list",
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
                    },
                    onRecipeClicked = { recipeId, recipe ->
                        (recipeId ?: recipe?.id)?.let { rId ->
                            model.baseUrl?.let { baseUrl ->
                                openBrowser(platformContext, "$baseUrl/view/recipe/$rId")
                            }
                        }

                    }
                )
            }

            Pages.FOODS -> allFoods?.let {
                Text(
                    "All foods:",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                foodListView(foods = it, showID = true) { food ->
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


