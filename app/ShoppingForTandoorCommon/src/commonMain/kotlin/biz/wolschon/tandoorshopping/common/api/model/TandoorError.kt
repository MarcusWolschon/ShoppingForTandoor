package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TandoorError(
    val detail: String
) : RuntimeException()