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