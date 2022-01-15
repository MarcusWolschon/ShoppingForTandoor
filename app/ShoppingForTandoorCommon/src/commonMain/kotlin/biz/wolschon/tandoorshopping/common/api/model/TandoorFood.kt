package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TandoorPagedFoodList (
    val count: Int,
    /**
     * fully qualified URL to get the next page
     */
    val next: String?,
    val previous: String?,
    val results: List<TandoorFood>
)

@Serializable
data class TandoorFood (
    val id: Int,
    val name: String,
    val description: String?,
    val ignore_shopping: Boolean,
    val supermarket_category: TandoorSupermarketCategory?
    //"parent": null,
    //"numchild": 0
) {

    val safeCategoryName
        get() = supermarket_category?.name ?: ""

    val safeCategoryId
        get() = supermarket_category?.id ?: -1

    class SortById(val inverted: Boolean = false) : Comparator<TandoorFood> {
        override fun compare(a: TandoorFood, b: TandoorFood) =
            if (inverted) {
                b.id.compareTo(a.id)
            } else {
                a.id.compareTo(b.id)
            }
    }

    class SortByCategory(val inverted: Boolean = false) : Comparator<TandoorFood> {
        private val secondary = SortByName(inverted)

        override fun compare(a: TandoorFood, b: TandoorFood): Int {
            val primarySort = if (inverted) {
                b.safeCategoryName.compareTo(a.safeCategoryName)
            } else {
                a.safeCategoryName.compareTo(b.safeCategoryName)
            }

            return if (primarySort == 0) {
                secondary.compare(a, b)
            } else {
                primarySort
            }
        }
    }

    class SortByName(val inverted: Boolean = false) : Comparator<TandoorFood> {
        override fun compare(a: TandoorFood, b: TandoorFood) =
            if (inverted) {
                b.name.compareTo(a.name)
            } else {
                a.name.compareTo(b.name)
            }
    }
}