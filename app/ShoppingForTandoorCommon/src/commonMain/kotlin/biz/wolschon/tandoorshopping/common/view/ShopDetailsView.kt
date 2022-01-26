package biz.wolschon.tandoorshopping.common.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorshopping.common.api.model.TandoorSupermarket

@Composable
fun shopDetailsView(shop: TandoorSupermarket) {

    // compose the UI elements

    Column(modifier = Modifier.fillMaxWidth()) {

        val labelModifier = Modifier.width(96.dp)

        Row {
            Text("ID:", labelModifier)
            Text(shop.id.toString())
        }

        Row {
            Text("Name:", labelModifier)
            Text(shop.name)
        }


        Row {
            Text("Description:", labelModifier)
            Text(shop.description ?: "-none-")
        }

        Row {
            Text("Categories:", labelModifier)
            Text("")
        }
        shop.categories.forEach { category ->
            Text("", labelModifier)
            Text( "${category.name} (${category.id})")
        }

    }
}


