package biz.wolschon.tandoorishopping.common.api.model

import kotlinx.serialization.Serializable

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
    val amount: Float,
    val order: Int,
    val checked: Boolean
)