package biz.wolschon.tandoorishopping.common.api.model

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class TandoorShoppingList (
    val id: Int,
    val uuid: String,
    val note: String?,
    //recipes
    val entries: Set<TandoorShoppingListEntry>,
    // "shared": [],
    val finished: Boolean,
    // "supermarket": null,
    //        "created_by": 1,
    //        "created_at": "2022-01-10T20:15:47.556349+01:00"
) {

    val sortedEntries
        get() = entries.sortedBy { it.food.name }.sortedBy { it.food.supermarket_category.name }

    class SortById(val inverted: Boolean = false) : Comparator<TandoorShoppingList> {
        override fun compare(a: TandoorShoppingList, b: TandoorShoppingList) =
            if (inverted) {
                b.id.compareTo(a.id)
            } else {
                a.id.compareTo(b.id)
            }
    }
    class SortByFinished(val inverted: Boolean = false) : Comparator<TandoorShoppingList> {
        override fun compare(a: TandoorShoppingList, b: TandoorShoppingList) =
            if (inverted) {
                b.finished.compareTo(a.finished)
            } else {
                a.finished.compareTo(b.finished)
            }
    }
    class SortByNote(val inverted: Boolean = false) : Comparator<TandoorShoppingList> {
        override fun compare(a: TandoorShoppingList, b: TandoorShoppingList) =
            if (inverted) {
                (b.note ?: "").compareTo(a.note ?: "")
            } else {
                (a.note ?: "").compareTo(b.note ?: "")
            }
    }
}

@Serializable
data class TandoorShoppingListEntry (
    val id: Int,
    val list_recipe: Int?,
    val food: TandoorFood,
    val unit: TandoorUnit?,
    val amount: String,
    val order: Int,
    val checked: Boolean
) {
    val amountBigDecimal
        get() = BigDecimal(amount)

    class SortById(val inverted: Boolean = false) : Comparator<TandoorShoppingListEntry> {
        override fun compare(a: TandoorShoppingListEntry, b: TandoorShoppingListEntry) =
            if (inverted) {
                b.id.compareTo(a.id)
            } else {
                a.id.compareTo(b.id)
            }
    }
    class SortByChecked(val inverted: Boolean = false) : Comparator<TandoorShoppingListEntry> {
        override fun compare(a: TandoorShoppingListEntry, b: TandoorShoppingListEntry) =
            if (inverted) {
                b.checked.compareTo(a.checked)
            } else {
                a.checked.compareTo(b.checked)
            }
    }
    class SortByCategory(val inverted: Boolean = false) : Comparator<TandoorShoppingListEntry> {
        override fun compare(a: TandoorShoppingListEntry, b: TandoorShoppingListEntry) =
            if (inverted) {
                b.food.supermarket_category.name.compareTo(a.food.supermarket_category.name)
            } else {
                a.food.supermarket_category.name.compareTo(b.food.supermarket_category.name)
            }
    }
    class SortByName(val inverted: Boolean = false) : Comparator<TandoorShoppingListEntry> {
        override fun compare(a: TandoorShoppingListEntry, b: TandoorShoppingListEntry) =
            if (inverted) {
                b.food.name.compareTo(a.food.name)
            } else {
                a.food.name.compareTo(b.food.name)
            }
    }
}