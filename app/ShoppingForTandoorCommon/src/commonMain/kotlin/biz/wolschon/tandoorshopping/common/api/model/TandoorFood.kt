@file:Suppress("PLUGIN_IS_NOT_ENABLED")

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

typealias TandoorFoodId=Int

@Serializable
data class TandoorFood (
    val id: TandoorFoodId,
    val name: String,
    val full_name: String,
    val description: String?,
    //recipe
    val food_onhand: Boolean,
    val ignore_shopping: Boolean = false,
    val supermarket_category: TandoorSupermarketCategory?,
    val parent: TandoorFoodId? = null,
    val numchild: Int = 0,
    val inherit_fields: List<InheritedField>? = listOf()
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


    @Suppress("unused")
    enum class InheritableField {supermarket_category, ignore_shopping}

    @Serializable
    data class InheritedField (
        val id: Int,
        val name: String,
        val field: InheritableField
    )
}
