package biz.wolschon.tandoorishopping.common.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList

@Preview
@Composable
fun shoppingListListPreview() {
    val shoppingLists = listOf(
        TandoorShoppingList(
            id = 0,
            uuid = "000",
            note = "get for sunday",
            entries = setOf(),
            finished = false
        ),
        TandoorShoppingList(
            id = 1,
            uuid = "111",
            note = "get from market",
            entries = setOf(),
            finished = false
        ),
        TandoorShoppingList(
            id = 2,
            uuid = "222",
            note = "alredy got these",
            entries = setOf(),
            finished = true
        )
    )
    shoppingListList(shoppingLists, showFinished = true)
}