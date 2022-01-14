package biz.wolschon.tandoorishopping.common.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TandooriShoppingList (
    val id: Int,
    val uuid: String,
    val note: String?,
    val finished: Boolean
)