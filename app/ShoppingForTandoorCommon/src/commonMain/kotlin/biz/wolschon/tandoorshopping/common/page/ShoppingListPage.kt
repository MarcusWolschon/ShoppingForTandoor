package biz.wolschon.tandoorshopping.common.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorshopping.common.Log
import biz.wolschon.tandoorshopping.common.NetworkDispatcher
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.api.model.*
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.openBrowser
import biz.wolschon.tandoorshopping.common.view.shoppingListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ShoppingListPage : Page() {
    override val title = "Shopping List"

    override val relativeUrl = "/list/shopping-list/"

    fun updateFoodEntry(
        model: Model,
        foodItem: TandoorShoppingListEntry,
        checked: Boolean,
        scope: CoroutineScope
    ) {

        scope.launch(NetworkDispatcher) {
            Log.d("ShoppingListPage", "updateShoppingListItemChecked")
            model.updateShoppingListItemChecked(foodItem.id, checked)
            //model.updateShoppingListItemChecked(foodItem.copy(checked = checked))
            Log.d("ShoppingListPage", "fetchShoppingList")
            model.fetchShoppingList()
            Log.d("ShoppingListPage", "fetchShoppingList done")
        }
    }

    @Composable
    private fun shopSelection(
        model: Model,
        currentSupermarket: TandoorSupermarket?,
        onSupermarketChanged: (TandoorSupermarketId?) -> Unit
    ) {
        val allSupermarkets = model.databaseModel.getCachedSupermarkets()
        var isExpanded by remember { mutableStateOf(false) }

        Row {
            Text(
                currentSupermarket?.name ?: "no supermarket",
                Modifier.weight(1f).height(48.dp).background(Color.LightGray)
            )
            Button(
                onClick = {
                    isExpanded = !isExpanded
                },
                Modifier.height(48.dp).width(48.dp)
            ) {
                Text(if (isExpanded) "^" else "v")
            }
        }

        if (isExpanded) {
            Column(Modifier.background(Color.LightGray)) {
                Row {
                    RadioButton(selected = (currentSupermarket == null),
                        onClick = {
                            if (currentSupermarket != null) {
                                onSupermarketChanged(null)
                                isExpanded = false
                            }
                        })
                    Text("no supermarket", Modifier.align(Alignment.CenterVertically))
                }

                allSupermarkets.forEach { market ->
                    Row {
                        RadioButton(selected = (currentSupermarket?.id == market.id),
                            onClick = {
                                if (currentSupermarket?.id != market.id) {
                                    onSupermarketChanged(market.id)
                                    isExpanded = false
                                }
                            })
                        Text(market.name, Modifier.align(Alignment.CenterVertically))
                    }
                }
            }
        }
    }

    @Composable
    override fun compose(
        model: Model,
        platformContext: PlatformContext,
        navigateTo: (Page) -> Unit
    ) {
        Log.d("ShoppingListPage", "compose() called")
        var showChecked by remember { mutableStateOf(model.settings.showCheckedShoppingEntries ?: false) }
        val scope = rememberCoroutineScope()
        val shoppingList = model.databaseModel.getLiveShoppingListEntries()
            .collectAsState(initial = model.databaseModel.getCachedShoppingListEntries())
            .value
        var currentSupermarketId by remember { mutableStateOf(model.settings.currentSupermarketID) }
        val currentSupermarket = currentSupermarketId?.let { model.databaseModel.getCachedSupermarkets().find { market -> market.id == it } }
        if (currentSupermarket == null && currentSupermarketId != null) {
            currentSupermarketId = null // prevent a no longer existing supermarket to be current
        }


        Row {
            Checkbox(checked = showChecked, onCheckedChange = { checked ->
                showChecked = checked
                model.settings.showCheckedShoppingEntries = checked
            })
            Text("show checked foods", Modifier.align(Alignment.CenterVertically))
        }

        shopSelection(model, currentSupermarket) { selection ->
            currentSupermarketId = selection
            model.settings.currentSupermarketID = selection
        }

        shoppingListView(
            entries = shoppingList,
            showFinished = showChecked,
            showID = false,
            currentSupermarket,
            onFoodCheckedChanged = { foodItem, checked ->
                updateFoodEntry(model, foodItem, checked, scope)
            },
            onFoodSelected = { food ->
                navigateTo(FoodPage(food))
            },
            onRecipeClicked = { _: ShopppingListRecipeId?, recipe: TandoorRecipeMealplan? ->
                recipe?.let { mealPlanRecipe ->
                    model.settings.baseUrl?.let { baseUrl ->
                        openBrowser(platformContext, "$baseUrl/view/recipe/${mealPlanRecipe.recipe}")
                    }
                }

            }
        )
    }

}