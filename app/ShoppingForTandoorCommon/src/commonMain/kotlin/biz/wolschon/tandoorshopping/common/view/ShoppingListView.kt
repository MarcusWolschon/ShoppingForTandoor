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
import biz.wolschon.tandoorshopping.common.api.model.*
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry.SortById
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry.SortByChecked
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry.SortByCategory
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry.SortByName
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry.SortByRecipe
import java.math.BigDecimal

@Composable
fun shoppingListView(entries: List<TandoorShoppingListEntry>,
                     showFinished: Boolean,
                     showID: Boolean = false,
                     currentSupermarket: TandoorSupermarket?,
                     onFoodCheckedChanged: (TandoorShoppingListEntry, Boolean) -> Unit,
                     onFoodSelected: (TandoorFood) -> Unit,
                     onRecipeClicked: (ShopppingListRecipeId?, TandoorRecipeMealplan?) -> Unit) {

    // state to be remembered

    var lastSorting by remember {
        val defaultSorting = currentSupermarket?.let { SortByCategory(currentSupermarket) } ?: SortByRecipe()
        mutableStateOf(defaultSorting)
    }

    // common layout data

    val idModifier = Modifier.width(48.dp)
    val checkedModifier = Modifier.width(65.dp)
    val categoryModifier = Modifier.width(100.dp)
    val amountModifier = Modifier.width(48.dp)
    val unitModifier = Modifier.width(64.dp)
    val nameModifier = Modifier.fillMaxWidth()
    val receiptModifier = Modifier.width(100.dp)

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
                    SortByCategory(currentSupermarket, inverted = true)
                } else {
                    SortByCategory(currentSupermarket)
                }
            }, categoryModifier) {
                Text("category")
            }
            Button(onClick = {
                lastSorting = if ((lastSorting as? SortByRecipe)?.inverted == false) {
                    SortByRecipe(inverted = true)
                } else {
                    SortByRecipe()
                }
            }, receiptModifier) {
                Text("recipe")
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

    fun formatAmount(amount: BigDecimal?): String {
        return amount?.stripTrailingZeros()?.toPlainString() ?: ""
    }

    /**
     * Render a header for a new category
     */
    @Composable
    fun shoppingListRecipe(
        recipeId: ShopppingListRecipeId?,
        recipe: TandoorRecipeMealplan?,
        onRecipeClicked: (ShopppingListRecipeId?, TandoorRecipeMealplan?) -> Unit) {
        Row(modifier = Modifier.fillMaxWidth()
            .background(Color.LightGray)
            .clickable {
                onRecipeClicked(recipeId, recipe)
            }) {
            Text(
                text = recipe?.name ?: recipeId?.let{ "Recipe #$recipeId" } ?: "No recipe",
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                color = Color.Blue
            )
        }
    }

    /**
     * Render a header for a new category
     */
    @Composable
    fun shoppingListCategory(foodCategory: TandoorSupermarketCategory,
                             showGreyed: Boolean) {
        val bgColor = if (showGreyed) {
            MaterialTheme.colors.secondary.copy(alpha = ContentAlpha.disabled)
        } else  {
            MaterialTheme.colors.secondary
        }

        val textColor = if (showGreyed) {
            MaterialTheme.colors.onSecondary.copy(alpha = ContentAlpha.disabled)
        } else  {
            MaterialTheme.colors.onSecondary
        }

        Row(modifier = Modifier.fillMaxWidth()
            .background(color = bgColor)
        ) {
            if (showID) {
                Spacer(idModifier)
            }
            Spacer(checkedModifier)

            Text(
                text = if (showGreyed) "[MISSING] ${foodCategory.name}" else foodCategory.name,
                color = textColor
            )
        }
    }

    /**
     * Render a row of data
     */
    @Composable
    fun shoppingListItemView(
        foodEntry: TandoorShoppingListEntry,
        onFoodCheckedChanged: (TandoorShoppingListEntry, Boolean) -> Unit,
        showGreyed: Boolean
    ) {

        val textColor = if (showGreyed) {
            MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.disabled)
        } else  {
            MaterialTheme.colors.onBackground
        }

        @Composable
        fun paintRow(
            foodEntry: TandoorShoppingListEntry,
            textColor: Color
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

                Text(
                    formatAmount(foodEntry.amountBigDecimal),
                    amountModifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.End,
                    color = textColor
                )
                Spacer(Modifier.width(1.dp))
                Text(
                    foodEntry.unit?.name ?: "-",
                    unitModifier.align(Alignment.CenterVertically),
                    textAlign = TextAlign.Start,
                    color = textColor
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

        val note = foodEntry.ingredient_note?.takeIf { it.isNotBlank() }
        if (note == null) {
            // single row
            paintRow(foodEntry, textColor)
        } else {
            // double height row to make space for note
            Row(modifier = Modifier.fillMaxWidth()) {
                Column (Modifier.fillMaxWidth().clickable { onFoodSelected.invoke(foodEntry.food) }) {
                    paintRow(foodEntry, textColor)
                    Text(
                        text = note,
                        modifier = nameModifier,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }

    // prepare the data to be shown

    val items: List<TandoorShoppingListEntry> =
        (if (showFinished) entries else entries.filter { !it.checked })
            .sortedWith(lastSorting)

    // compose the UI elements

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            shoppingListItemHeader()
        }
        items(items.size) { index ->
            val item = items[index]
            if (lastSorting is SortByRecipe) {
                if (index == 0 || items[index - 1].list_recipe != item.list_recipe) {
                    shoppingListRecipe(item.list_recipe, item.recipe_mealplan, onRecipeClicked)
                }
            }
            val category = item.food.supermarket_category
            val greyed = category != null &&
                    currentSupermarket != null &&
                    !currentSupermarket.hasCategory(category)
            if (index == 0 || items[index - 1].food.safeCategoryId != item.food.safeCategoryId) {
                item.food.supermarket_category?.let { shoppingListCategory(it, showGreyed = greyed) }
            }
            shoppingListItemView(item, onFoodCheckedChanged, showGreyed = greyed)
        }

        item {
            // don't obscure the last list entry by the Floating Action Button
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(64.dp))
            }
        }

    }
}


