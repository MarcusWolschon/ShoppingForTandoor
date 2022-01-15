package biz.wolschon.tandoorishopping.common.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorishopping.common.api.model.TandoorFood
import biz.wolschon.tandoorishopping.common.api.model.TandoorFood.SortById
import biz.wolschon.tandoorishopping.common.api.model.TandoorFood.SortByCategory
import biz.wolschon.tandoorishopping.common.api.model.TandoorFood.SortByName
import biz.wolschon.tandoorishopping.common.api.model.TandoorSupermarketCategory

@Composable
fun foodListView(
    foods: List<TandoorFood>,
    showID: Boolean = false,
    onFoodSelected: (TandoorFood) -> Unit
) {

    // state to be remembered

    var lastSorting by remember { mutableStateOf<Comparator<TandoorFood>>(SortByCategory()) }

    // common layout data

    val idModifier = Modifier.width(48.dp)
    val categoryModifier = Modifier.width(100.dp)
    val nameModifier = Modifier.fillMaxWidth()

    /**
     * Render item headers with buttons for sorting
     */
    @Composable
    fun foodListItemHeader() {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (showID) {
                Button(onClick = {
                    lastSorting = if ((lastSorting as? SortById)?.inverted == false) {
                        SortById(inverted = true)
                    } else {
                        SortById()
                    }
                }, idModifier) {
                    Text("ID")
                }
            }
            Button(onClick = {
                lastSorting = if ((lastSorting as? SortByCategory)?.inverted == false) {
                    SortByCategory(inverted = true)
                } else {
                    SortByCategory()
                }
            }, categoryModifier) {
                Text("category")
            }
            Button(onClick = {
                lastSorting = if ((lastSorting as? SortByName)?.inverted == false) {
                    SortByName(inverted = true)
                } else {
                    SortByName()
                }
            }, nameModifier) {
                Text("name")
            }
        }
    }

    /**
     * Render a header for a new category
     */
    @Composable
    fun foodListCategory(foodCategory: TandoorSupermarketCategory) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (showID) {
                Spacer(idModifier)
            }
            Text(foodCategory.name)
        }
    }

    /**
     * Render a row of data
     */
    @Composable
    fun foodListItemView(
        foodEntry: TandoorFood,
        onFoodSelected: (TandoorFood) -> Unit
    ) {
        Row(modifier = Modifier.fillMaxWidth().clickable { onFoodSelected.invoke(foodEntry) }) {
            if (showID) {
                Text("${foodEntry.id}", idModifier)
            }
            Text(
                foodEntry.name,
                nameModifier.align(Alignment.CenterVertically),
                textAlign = TextAlign.End
            )
        }
    }

    // prepare the data to be shown

    val items: List<TandoorFood> = foods.sortedWith(lastSorting)

    // compose the UI elements

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items.size + 1) { index ->
            when (index) {
                0 -> foodListItemHeader()
                else -> {
                    val item = items[index - 1]
                    if (index == 1 || items[index - 2].safeCategoryId != item.safeCategoryId) {
                        item.supermarket_category?.let { foodListCategory(it) }
                    }
                    foodListItemView(item, onFoodSelected)
                }
            }

        }

    }
}


