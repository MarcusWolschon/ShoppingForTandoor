package biz.wolschon.tandoorishopping.common.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TandoorUnit (
    val id: Int,
    val name: String,
    val description: String?,
)