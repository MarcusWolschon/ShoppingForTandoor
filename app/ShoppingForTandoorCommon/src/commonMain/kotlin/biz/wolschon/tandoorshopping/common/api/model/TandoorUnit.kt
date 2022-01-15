package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TandoorUnit (
    val id: Int,
    val name: String,
    val description: String?,
)