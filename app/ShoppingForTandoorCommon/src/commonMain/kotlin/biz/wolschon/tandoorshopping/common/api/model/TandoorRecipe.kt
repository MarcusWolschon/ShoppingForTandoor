package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

typealias ShopppingListRecipeId=Int

@Serializable
data class TandoorShoppingListRecipe (
    val id: ShopppingListRecipeId,
    val recipe: Int,
    val recipe_name: String,
    val servings: Float
)

typealias RecipeId=Int

@Serializable
data class TandoorRecipe (
    val id: RecipeId,
    val name: String,
    /**
     * URL
     */
    val image: String?,
    val servings: Int
    )