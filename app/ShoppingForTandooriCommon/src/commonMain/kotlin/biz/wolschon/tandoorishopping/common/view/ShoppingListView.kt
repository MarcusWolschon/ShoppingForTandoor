package biz.wolschon.tandoorishopping.common.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorishopping.common.api.model.TandoorFood
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry.SortById
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry.SortByChecked
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry.SortByCategory
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry.SortByName
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry
import biz.wolschon.tandoorishopping.common.api.model.TandoorSupermarketCategory
import java.math.BigDecimal

@Composable
fun shoppingListView(shoppingList: TandoorShoppingList,
                     showFinished: Boolean,
                     showID: Boolean = false,
                     onFoodCheckedChanged: (TandoorShoppingListEntry, Boolean) -> Unit,
                     onFoodSelected: (TandoorFood) -> Unit) {

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

    fun formatAmount(amount: BigDecimal): String {
        //val isInteger = amount.stripTrailingZeros().scale() <= 0
        return amount.stripTrailingZeros().toPlainString()
    }

    /**
     * Render a header for a new category
     */
    @Composable
    fun shoppingListCategory(foodCategory: TandoorSupermarketCategory) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (showID) {
                Spacer(idModifier)
            }
            Spacer(checkedModifier)
            Text(foodCategory.name)
        }
    }

    /**
     * Render a row of data
     */
    @Composable
    fun shoppingListItemView(
        foodEntry: TandoorShoppingListEntry,
        onFoodCheckedChanged: (TandoorShoppingListEntry, Boolean) -> Unit
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (showID) {
                Text("${foodEntry.id}", idModifier)
            }
            Checkbox(
                checked = foodEntry.checked,
                onCheckedChange = { onFoodCheckedChanged(foodEntry, it) },
                modifier = checkedModifier.align(Alignment.CenterVertically)
            )
            /*Text(
                foodEntry.food.supermarket_category.name,
                categoryModifier.align(Alignment.CenterVertically),
            )*/
            Text(
                formatAmount(foodEntry.amountBigDecimal),
                amountModifier.align(Alignment.CenterVertically),
                textAlign = TextAlign.End
            )
            Spacer(Modifier.width(1.dp))
            Text(
                foodEntry.unit?.name ?: "-",
                unitModifier.align(Alignment.CenterVertically),
                textAlign = TextAlign.Start
            )
            Text(
                text = foodEntry.food.name,
                modifier = nameModifier
                    .align(Alignment.CenterVertically)
                    .clickable { onFoodSelected.invoke(foodEntry.food) },
                textAlign = TextAlign.End,
                textDecoration = TextDecoration.Underline,
                color = Color.Blue
            )
        }
    }

    // prepare the data to be shown

    val items: List<TandoorShoppingListEntry> =
        (if (showFinished) shoppingList.entries else shoppingList.entries.filter { !it.checked })
            .sortedWith(lastSorting)

    // compose the UI elements

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items.size + 1) { index ->
            when (index) {
                0 -> shoppingListItemHeader()
                else -> {
                    val item = items[index - 1]
                    if (index == 1 || items[index - 2].food.safeCategoryId != item.food.safeCategoryId) {
                        item.food.supermarket_category?.let { shoppingListCategory(it) }
                    }
                    shoppingListItemView(item, onFoodCheckedChanged)
                }
            }

        }

    }
}


