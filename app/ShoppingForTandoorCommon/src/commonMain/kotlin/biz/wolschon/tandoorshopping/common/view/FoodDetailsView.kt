package biz.wolschon.tandoorshopping.common.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorshopping.common.api.model.TandoorFood

@Composable
fun foodDetailsView(food: TandoorFood) {

    // compose the UI elements

    Column(modifier = Modifier.fillMaxWidth()) {

        val labelModifier = Modifier.width(96.dp)

        Row {
            Text("ID:", labelModifier)
            Text(food.id.toString())
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
            Text("Description:", labelModifier)
            Text(food.description ?: "-none-")
        }

    }
}


