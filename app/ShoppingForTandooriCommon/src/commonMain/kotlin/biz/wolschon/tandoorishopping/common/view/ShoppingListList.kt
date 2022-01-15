package biz.wolschon.tandoorishopping.common.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList.SortById
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList.SortByFinished
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList.SortByNote

@Composable
fun shoppingListList(shoppingLists: List<TandoorShoppingList>,
                     showFinished: Boolean,
                     onClick: (TandoorShoppingList) -> Unit) {

    // state to be remembered

    var lastSorting by remember { mutableStateOf<Comparator<TandoorShoppingList>>(SortById()) }

    // common layout data

    val idModifier = Modifier.width(48.dp)
    val finishedModifier = Modifier.width(100.dp)
    val noteModifier = Modifier.fillMaxWidth()

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
                lastSorting = if ((lastSorting as? SortByFinished)?.inverted == false) {
                    SortByFinished(inverted = true)
                } else {
                    SortByFinished()
                }
            }, finishedModifier) {
                Text("finished?")
            }
            Button(onClick = {
                lastSorting = if ((lastSorting as? SortByNote)?.inverted == false) {
                    SortByNote(inverted = true)
                } else {
                    SortByNote()
                }
            }, noteModifier) {
                Text("note")
            }
        }
    }

    /**
     * Render a row of data
     */
    @Composable
    fun shoppingListItemView(shoppingList: TandoorShoppingList) {
        Row(modifier = Modifier.fillMaxWidth().clickable { onClick(shoppingList) }) {
            Text("${shoppingList.id}", idModifier)
            Text(if (shoppingList.finished) "finished" else "open", finishedModifier)
            Text(shoppingList.note ?: "---", noteModifier)
        }
    }

    // prepare the data to be shown

    val items: List<TandoorShoppingList> =
        (if (showFinished) shoppingLists else shoppingLists.filter { !it.finished })
            .sortedWith(lastSorting)

    // compose the UI elements

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items.size + 1) { index ->
            when(index) {
                0 -> shoppingListItemHeader()
                else -> shoppingListItemView(items[index - 1])
            }

        }

    }
}


