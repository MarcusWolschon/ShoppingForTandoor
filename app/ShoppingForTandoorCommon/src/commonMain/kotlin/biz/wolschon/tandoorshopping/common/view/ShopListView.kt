package biz.wolschon.tandoorshopping.common.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import biz.wolschon.tandoorshopping.common.api.model.TandoorSupermarket
import biz.wolschon.tandoorshopping.common.api.model.TandoorSupermarket.SortById
import biz.wolschon.tandoorshopping.common.api.model.TandoorSupermarket.SortByName
import java.util.*
import kotlin.Comparator

@Composable
fun shopListView(
    shops: List<TandoorSupermarket>,
    showID: Boolean = false,
    onShopSelected: (TandoorSupermarket) -> Unit
) {

    // state to be remembered

    var sarchFor by remember { mutableStateOf("") }
    var lastSorting by remember { mutableStateOf<Comparator<TandoorSupermarket>>(SortByName()) }

    // common layout data

    val idModifier = Modifier.width(48.dp)
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
    fun shopListItemView(
        shopEntry: TandoorSupermarket,
        onShopSelected: (TandoorSupermarket) -> Unit
    ) {
        Row(modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .clickable { onShopSelected.invoke(shopEntry) }
        ) {
            if (showID) {
                Text("${shopEntry.id}", idModifier)
            }
            Text(
                shopEntry.name,
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

    val items: List<TandoorSupermarket> = shops
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
                    shopListItemView(item, onShopSelected)
        }
        item {
            // don't obscure the last list entry by the Floating Action Button
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(64.dp))
            }
        }
    }
}


