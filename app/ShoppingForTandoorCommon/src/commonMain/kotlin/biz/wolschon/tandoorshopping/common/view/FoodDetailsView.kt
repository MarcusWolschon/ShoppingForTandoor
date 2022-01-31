package biz.wolschon.tandoorshopping.common.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorshopping.common.api.model.TandoorFood

@Composable
fun foodDetailsView(food: TandoorFood,
                    allFoods: List<TandoorFood>,
                    onFoodSelected: (TandoorFood) -> Unit,
                    onFoodOnHandChanged: (TandoorFood, Boolean) -> Unit) {

    // compose the UI elements

    Column(modifier = Modifier.fillMaxWidth()) {

        val labelModifier = Modifier.width(96.dp)

        Row {
            Text("ID:", labelModifier)
            Text(food.id.toString())
        }

        Row {
            Text("Parent food:", labelModifier)
            allFoods.find { it.id == food.parent }
                ?.let { parent ->
                    Text(
                        parent.name,
                        Modifier.clickable { onFoodSelected.invoke(parent) },
                        textDecoration = TextDecoration.Underline,
                        color = Color.Blue
                    )
                }
                ?: run {
                    Text(
                        food.parent?.toString() ?: "--none--"
                    )
                }

        }

        Row {
            Text("Name:", labelModifier)
            Text(food.name)
        }

        Row {
            Text("Category:", labelModifier)
            Text(food.supermarket_category?.let { "${it.name} (${it.id})" } ?: "-none-")
        }



        Row {
            Text("Child foods:", labelModifier)
            Column {
                allFoods.filter { it.parent == food.id }.forEach { child ->
                    Text(
                        child.name,
                        Modifier.clickable { onFoodSelected.invoke(child) }.height(48.dp),
                        textDecoration = TextDecoration.Underline,
                        color = Color.Blue
                    )
                }
            }
        }


        Row {
            Text("Description:", labelModifier)
            Text(food.description ?: "-none-")
        }

        Row {
            Text("Ignore Shopping:", labelModifier)
            Text(if (food.ignore_shopping) "yes" else "no")
        }

        Row {
            Text("On hand:", labelModifier.align(Alignment.CenterVertically))
            Checkbox(checked = food.food_onhand, onCheckedChange = { checked -> onFoodOnHandChanged(food, checked) })
        }

    }
}


