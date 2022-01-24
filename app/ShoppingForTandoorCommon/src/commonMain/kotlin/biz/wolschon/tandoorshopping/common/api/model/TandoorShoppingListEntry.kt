@file:Suppress("PLUGIN_IS_NOT_ENABLED")
package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

typealias TandoorShoppingListEntryId=Int

@Serializable
data class TandoorShoppingListEntry (
    val id: TandoorShoppingListEntryId,
    val list_recipe: ShopppingListRecipeId?,
    val food: TandoorFood,
    val unit: TandoorUnit?,
    val ingredient: Int?,
    //val ingredient_note: String?, //TODO: Field 'ingredient_note' is required for type with serial name 'biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry', but it was missing
    @Transient
    var recipe: TandoorRecipe? = null,
    val amount: Float?,
    val order: Int?,
    val checked: Boolean,
    val recipe_mealplan: TandoorRecipeMealplan?,
    val created_by: TandoorUser,
    val created_at: String,
    val completed_at: String?
    //delay_until
) {
    val amountBigDecimal
        get() = amount?.toBigDecimal()

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
                b.food.safeCategoryName.compareTo(a.food.safeCategoryName)
            } else {
                a.food.safeCategoryName.compareTo(b.food.safeCategoryName)
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

    class SortByRecipe(val inverted: Boolean = false) : Comparator<TandoorShoppingListEntry> {
        override fun compare(a: TandoorShoppingListEntry, b: TandoorShoppingListEntry) =
            if (inverted) {
                (b.list_recipe ?: -1).compareTo(a.list_recipe ?: -1)
            } else {
                (a.list_recipe ?: -1).compareTo(b.list_recipe ?: -1)
            }
    }
}