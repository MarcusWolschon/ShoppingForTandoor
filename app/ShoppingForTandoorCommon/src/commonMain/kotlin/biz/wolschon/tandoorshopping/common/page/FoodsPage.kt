package biz.wolschon.tandoorshopping.common.page

import androidx.compose.runtime.*
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
        val allFoods = model.databaseModel.getLiveFoods()
            .collectAsState(initial = model.databaseModel.getCachedFoods())
            .value

        foodListView(foods = allFoods, showID = true) { food ->
            navigateTo(FoodPage(food))
        }
    }
}