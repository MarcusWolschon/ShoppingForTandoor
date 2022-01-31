package biz.wolschon.tandoorshopping.common.page

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.view.foodListView

class FoodsPage: Page() {

    override val title = "All Foods"

    override val relativeUrl = "/list/food/"

    @Composable
    override fun compose(model: Model,
                         platformContext: PlatformContext,
                         navigateTo: (Page) -> Unit) {
        var showOnHand by remember { mutableStateOf(model.settings.showOnlyOnHandFoods ?: false) }
        val allFoods = model.databaseModel.getLiveFoods()
            .collectAsState(initial = model.databaseModel.getCachedFoods())
            .value

        Row {
            Checkbox(checked = showOnHand, onCheckedChange = { checked ->
                showOnHand = checked
                model.settings.showOnlyOnHandFoods = checked
            })
            Text("show only foods on hand", Modifier.align(Alignment.CenterVertically))
        }

        foodListView(
            foods = if (showOnHand) allFoods.filter { it.food_onhand } else allFoods,
            showID = true
        ) { food ->
            navigateTo(FoodPage(food))
        }
    }
}