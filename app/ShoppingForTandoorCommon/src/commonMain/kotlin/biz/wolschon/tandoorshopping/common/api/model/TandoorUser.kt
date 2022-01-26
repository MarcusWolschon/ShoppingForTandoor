@file:Suppress("PLUGIN_IS_NOT_ENABLED")
package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

typealias TandoorUserId=Int

@Serializable
data class TandoorUser (
    val id: TandoorUserId,
    val username: String
)