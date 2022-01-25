@file:Suppress("PLUGIN_IS_NOT_ENABLED")
package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

typealias TandoorSupermarketId=Int

@Serializable
data class TandoorSupermarket (
    val id: TandoorSupermarketId,
    val name: String,
    val description: String?,
    val category_to_supermarket: Set<TandoorSupermarketToCategory>
) {
    fun hasCategory(category: TandoorSupermarketCategory?) =
        category_to_supermarket.any { it.category.id == category?.id }

    val categories
        get() = category_to_supermarket
            .sortedBy { it.order }
            .map { it.category }



    class SortById(val inverted: Boolean = false) : Comparator<TandoorSupermarket> {
        override fun compare(a: TandoorSupermarket, b: TandoorSupermarket) =
            if (inverted) {
                b.id.compareTo(a.id)
            } else {
                a.id.compareTo(b.id)
            }
    }

    class SortByName(val inverted: Boolean = false) : Comparator<TandoorSupermarket> {
        override fun compare(a: TandoorSupermarket, b: TandoorSupermarket) =
            if (inverted) {
                b.name.compareTo(a.name)
            } else {
                a.name.compareTo(b.name)
            }
    }
}

@Serializable
data class TandoorSupermarketToCategory (
    val id: Int,
    val category: TandoorSupermarketCategory,
    val supermarket: TandoorSupermarketId,
    val order: Int
)

@Serializable
data class TandoorSupermarketCategory (
    val id: Int,
    val name: String,
    val description: String?
)