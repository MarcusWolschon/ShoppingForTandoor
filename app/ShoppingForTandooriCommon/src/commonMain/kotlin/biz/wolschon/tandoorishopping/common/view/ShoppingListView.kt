package biz.wolschon.tandoorishopping.common.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry.SortById
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry.SortByChecked
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry.SortByCategory
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry.SortByName
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry

@Composable
fun shoppingListView(shoppingList: TandoorShoppingList,
                     showFinished: Boolean,
                     onFoodCheckedChanged: (TandoorShoppingListEntry, Boolean) -> Unit) {

    // state to be remembered

    var lastSorting by remember { mutableStateOf<Comparator<TandoorShoppingListEntry>>(SortById()) }

    // common layout data

    val idModifier = Modifier.width(48.dp)
    val checkedModifier = Modifier.width(65.dp)
    val categoryModifier = Modifier.width(100.dp)
    val amountModifier = Modifier.width(32.dp)
    val unitModifier = Modifier.width(48.dp)
    val nameModifier = Modifier.fillMaxWidth()

    /**
     * Render item headers with buttons for sorting
     */
    @Composable
    fun shoppingListItemHeader() {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                lastSorting = if ((lastSorting as? SortById)?.inverted == false) {
                    SortById(inverted = true)
                } else {
                    SortById()
                }
            }, idModifier) {
                Text("ID")
            }
            Button(onClick = {
                lastSorting = if ((lastSorting as? SortByChecked)?.inverted == false) {
                    SortByChecked(inverted = true)
                } else {
                    SortByChecked()
                }
            }, checkedModifier) {
                Text("✓")
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
     * Render a row of data
     */
    @Composable
    fun shoppingListItemView(foodEntry: TandoorShoppingListEntry,
                             onFoodCheckedChanged: (TandoorShoppingListEntry, Boolean) -> Unit) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("${foodEntry.id}", idModifier)
            Checkbox(checked = foodEntry.checked,
                onCheckedChange = { onFoodCheckedChanged(foodEntry, it) },
                modifier = checkedModifier.align(Alignment.CenterVertically)
            )
            Text(foodEntry.food.supermarket_category.name, categoryModifier)
            Text(foodEntry.amount.toString(), amountModifier)
            Text(foodEntry.unit?.name ?: "---", unitModifier)
        }
    }

    // prepare the data to be shown

    val items: List<TandoorShoppingListEntry> =
        (if (showFinished) shoppingList.entries else shoppingList.entries.filter { !it.checked })
            .sortedWith(lastSorting)

    // compose the UI elements

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items.size + 1) { index ->
            when(index) {
                0 -> shoppingListItemHeader()
                else -> shoppingListItemView(items[index - 1], onFoodCheckedChanged)
            }

        }

    }
}


