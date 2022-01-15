package biz.wolschon.tandoorishopping.common.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TandoorSupermarket (
    val id: Int,
    val name: String,
    val description: String?,
    val category_to_supermarket: Set<TandoorSupermarketCategory>
) {
    val categories
        get() = category_to_supermarket.sortedBy { it.name }
}

@Serializable
data class TandoorSupermarketCategory (
    val id: Int,
    val name: String,
    val description: String?
)