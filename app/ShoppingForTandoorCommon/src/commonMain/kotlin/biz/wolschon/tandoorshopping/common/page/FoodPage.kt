package biz.wolschon.tandoorshopping.common.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorshopping.common.NetworkDispatcher
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.api.model.TandoorFood
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.view.foodDetailsView
import kotlinx.coroutines.launch

class FoodPage(private val food: TandoorFood): Page() {

    override val title = food.full_name.takeIf { it.isNotBlank() } ?: food.name

    @Composable
    override fun compose(model: Model,
                         platformContext: PlatformContext,
                         navigateTo: (Page) -> Unit) {
        // scope that lasts until we navigate away from this page
        val scope = rememberCoroutineScope()
        val liveFood = model.databaseModel.getLiveFood(food.id).collectAsState(food)
        val allFoods = model.databaseModel.getLiveFoods().collectAsState(listOf())
        var isRefreshingMessage by remember { mutableStateOf<String?>(null) }

        Column(modifier = Modifier.fillMaxWidth()) {

            isRefreshingMessage?.let { message ->
                    Row(
                        Modifier
                            .background(Color.Yellow)
                            .fillMaxWidth()
                            .height(48.dp),
                    ) {
                        Text(
                            text = message,
                            color = Color.Black,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 16.dp, vertical = 0.dp)
                        )
                    }
                }

            foodDetailsView(
                food = liveFood.value ?: food,
                allFoods = allFoods.value,
                onFoodSelected = { food ->
                    navigateTo(FoodPage(food))
                },
                onFoodOnHandChanged = { food, onHand ->
                    scope.launch(NetworkDispatcher) {
                        isRefreshingMessage = "updating food..."
                        model.updateFoodItemOnHand(food.id, onHand)
                        model.fetchFood(food.id)
                        isRefreshingMessage = null
                    }
                }
            )
        }
    }
}