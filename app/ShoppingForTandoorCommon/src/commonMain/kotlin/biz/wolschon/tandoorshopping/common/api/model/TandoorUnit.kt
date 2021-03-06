@file:Suppress("PLUGIN_IS_NOT_ENABLED")
package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

typealias TandoorUnitId=Int

@Serializable
data class TandoorUnit (
    val id: TandoorUnitId,
    val name: String,
    val description: String?,
)