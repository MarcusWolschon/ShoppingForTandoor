@file:Suppress("PLUGIN_IS_NOT_ENABLED")
package biz.wolschon.tandoorshopping.common.api.model

import kotlinx.serialization.Serializable

typealias TandoorRecipeMealplanId=Int

@Serializable
data class TandoorRecipeMealplan (
    val id: TandoorRecipeMealplanId,
    /**
     * "recipe_name": "Apfel-Sellerie-Suppe",
     */
    val recipe_name: String,
    /**
     * "name": "Apfel Sellerie Suppe (12)",
     */
    val name: String,
    val recipe: Int,
    val mealplan: Int,
    val servings: Float,
    val mealplan_note: String
)
