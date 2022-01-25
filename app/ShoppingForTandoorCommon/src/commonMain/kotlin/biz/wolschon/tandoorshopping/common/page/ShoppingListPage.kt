package biz.wolschon.tandoorshopping.common.page

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import biz.wolschon.tandoorshopping.common.NetworkDispatcher
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry
import biz.wolschon.tandoorshopping.common.api.model.TandoorSupermarket
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.openBrowser
import biz.wolschon.tandoorshopping.common.view.shoppingListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ShoppingListPage : Page() {
    override val title = "Shopping List"

    fun updateFoodEntry(model: Model, foodItem: TandoorShoppingListEntry, checked: Boolean, scope: CoroutineScope) {

        scope.launch(NetworkDispatcher) {
            model.updateShoppingListItemChecked(foodItem.id, checked)
            //model.updateShoppingListItemChecked(foodItem.copy(checked = checked))
            model.fetchShoppingList()
        }
    }

    @Composable
    override fun compose(model: Model,
                         platformContext: PlatformContext,
                         navigateTo: (Page) -> Unit) {
        var showChecked by remember { mutableStateOf(false) }
        val allSupermarkets = model.databaseModel.getCachedSupermarkets()
        val shoppingList = model.databaseModel.getCachedShoppingListEntries()
        var currentSupermarket by remember { mutableStateOf<TandoorSupermarket?>(null) }
        val scope = rememberCoroutineScope()


        Row {
            Checkbox(checked = showChecked, onCheckedChange = { checked -> showChecked = checked })
            Text("show checked foods", Modifier.align(Alignment.CenterVertically))
        }
        allSupermarkets.forEach { market ->
            Row {
                Checkbox(checked = (currentSupermarket?.id == market.id),
                    onCheckedChange = { checked ->
                        if (checked && currentSupermarket?.id != market.id) {
                            currentSupermarket = market
                        }
                    })
                Text(market.name, Modifier.align(Alignment.CenterVertically))
            }
        }
        shoppingListView(
            entries = shoppingList,
            showFinished = showChecked,
            showID = false,
            currentSupermarket,
            onFoodCheckedChanged = { foodItem, checked -> updateFoodEntry(model, foodItem, checked, scope) },
            onFoodSelected = { food ->
                navigateTo(FoodPage(food))
            },
            onRecipeClicked = { recipeId, recipe ->
                (recipeId ?: recipe?.id)?.let { rId ->
                    model.settings.baseUrl?.let { baseUrl ->
                        openBrowser(platformContext, "$baseUrl/view/recipe/$rId")
                    }
                }

            }
        )

    }
}