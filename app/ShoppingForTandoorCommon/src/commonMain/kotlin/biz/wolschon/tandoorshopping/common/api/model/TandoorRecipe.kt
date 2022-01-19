package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

@Serializable
data class TandoorRecipe (
    val id: Int,
    val name: String,
    /**
     * URL
     */
    val image: String?,
    val servings: Int
    )