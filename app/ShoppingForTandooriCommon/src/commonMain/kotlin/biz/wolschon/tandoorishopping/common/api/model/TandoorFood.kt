package biz.wolschon.tandoorishopping.common.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TandoorFood (
    val id: Int,
    val name: String,
    val description: String?,
    val ignore_shopping: Boolean,
    val supermarket_category: TandoorSupermarketCategory
    //"parent": null,
    //"numchild": 0
)