package biz.wolschon.tandoorshopping.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorshopping.common.api.model.TandoorFood
import biz.wolschon.tandoorshopping.common.api.model.TandoorFood.SortById
import biz.wolschon.tandoorshopping.common.api.model.TandoorFood.SortByCategory
import biz.wolschon.tandoorshopping.common.api.model.TandoorFood.SortByName
import biz.wolschon.tandoorshopping.common.api.model.TandoorSupermarketCategory
import java.util.*
import kotlin.Comparator

@Composable
fun foodListView(
    foods: List<TandoorFood>,
    showID: Boolean = false,
    onFoodSelected: (TandoorFood) -> Unit
) {

    // state to be remembered

    var sarchFor by remember { mutableStateOf("") }
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
            Button(
                onClick = {
                    lastSorting = if ((lastSorting as? SortByName)?.inverted == false) {
                        SortByName(inverted = true)
                    } else {
                        SortByName()
                    }
                }, nameModifier
            ) {
                Text(
                    "name"
                )
            }
        }
    }

    /**
     * Render a header for a new category
     */
    @Composable
    fun foodListCategory(foodCategory: TandoorSupermarketCategory) {
        Row(modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colors.secondary)
        ) {
            if (showID) {
                Spacer(idModifier)
            }
            Text(foodCategory.name, color = MaterialTheme.colors.onSecondary)
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
        Row(modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .clickable { onFoodSelected.invoke(foodEntry) }) {
            if (showID) {
                Text("${foodEntry.id}", idModifier)
            }
            Text(
                foodEntry.name,
                nameModifier.align(Alignment.CenterVertically),
                textAlign = TextAlign.End,
                textDecoration = TextDecoration.Underline,
                color = Color.Blue
            )
        }
    }

    // prepare the data to be shown

    val locale = Locale.getDefault()
    val searchTemp = sarchFor.lowercase(locale)

    val items: List<TandoorFood> = foods
        .filter { it.name.lowercase(locale).contains(searchTemp) }
        .sortedWith(lastSorting)

    // compose the UI elements

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
                TextField(
                    value = sarchFor,
                    label = { Text("Search") },
                    onValueChange = { sarchFor = it },
                    modifier = Modifier.fillMaxWidth()
                )
        }
        item {
            foodListItemHeader()
        }

        items(items.size) { index ->
                    val item = items[index]
                    if (index == 0 || items[index - 1].safeCategoryId != item.safeCategoryId) {
                        item.supermarket_category?.let { foodListCategory(it) }
                    }
                    foodListItemView(item, onFoodSelected)
        }


        item {
            // don't obscure the last list entry by the Floating Action Button
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(64.dp))
            }
        }

    }
}


