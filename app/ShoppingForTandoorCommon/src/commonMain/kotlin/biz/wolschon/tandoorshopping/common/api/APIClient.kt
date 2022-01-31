@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED", "PLUGIN_IS_NOT_ENABLED")
package biz.wolschon.tandoorshopping.common.api

import biz.wolschon.tandoorshopping.common.Log
import biz.wolschon.tandoorshopping.common.api.model.*
import io.ktor.client.request.*
import io.ktor.http.*
import biz.wolschon.tandoorshopping.common.getHttpClient
import io.ktor.client.features.*
import kotlinx.serialization.Serializable

class APIClient {

    suspend fun fetchFoods(baseurl: String, accessToken: String) =
        getHttpClient().get<TandoorPagedFoodList> {
            url("$baseurl/food/")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }

    suspend fun fetchFood(baseurl: String, accessToken: String, id: TandoorFoodId) =
        getHttpClient().get<TandoorFood> {
            url("$baseurl/food/$id")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }

    suspend fun fetchMoreFoods(fullyUrl: String, accessToken: String) =
        getHttpClient().get<TandoorPagedFoodList> {
            url(fullyUrl)
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }

    suspend fun fetchShoppingListRecipe(baseurl: String, accessToken: String, recipeId: ShopppingListRecipeId) =
        getHttpClient().get<TandoorShoppingListRecipe> {
            url("$baseurl/shopping-list-recipe/$recipeId")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }

    suspend fun fetchRecipe(baseurl: String, accessToken: String, recipeId: RecipeId) =
        getHttpClient().get<TandoorRecipe> {
            url("$baseurl/recipe/$recipeId")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }

    suspend fun fetchShoppingList(baseurl: String, accessToken: String): List<TandoorShoppingListEntry> {
        Log.i("APIClient", "fetchShoppingLists() HttpClient")
        val result =  getHttpClient().get<List<TandoorShoppingListEntry>> {
            url("$baseurl/shopping-list-entry/")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }
        Log.i("APIClient", "fetchShoppingLists() HttpClient returned")
        return result
    }

    suspend fun fetchSupermarkets(baseurl: String, accessToken: String): List<TandoorSupermarket> {
        Log.i("APIClient", "fetchSupermarkets() HttpClient")
        val result =  getHttpClient().get<List<TandoorSupermarket>> {
            url("$baseurl/supermarket/")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }
        Log.i("APIClient", "fetchSupermarkets() HttpClient returned")
        return result
    }

    @Serializable
    data class ShoppingListEntryUpdate(val id: TandoorShoppingListEntryId, val checked: Boolean)

    suspend fun updateShoppingListItemChecked(baseurl: String,
                                              accessToken: String,
                                              entryId: TandoorShoppingListEntryId,
                                              checked: Boolean) {
        try {
            getHttpClient().patch<TandoorShoppingListEntry> {
                url("$baseurl/shopping-list-entry/$entryId/")
                contentType(ContentType.Application.Json)
                header("Authorization", "Token $accessToken")
                body = ShoppingListEntryUpdate(entryId, checked)
            }
        } catch (x: RedirectResponseException) {
            //ignored
        }
    }

    @Serializable
    data class FoodEntryOnHandUpdate(val id: TandoorFoodId, val food_onhand: Boolean)

    suspend fun updateFoodItemOnHand(baseurl: String,
                                     accessToken: String,
                                     entryId: TandoorFoodId,
                                     omHand: Boolean) {
        try {
            getHttpClient().patch<TandoorFood> {
                url("$baseurl/food/$entryId/")
                contentType(ContentType.Application.Json)
                header("Authorization", "Token $accessToken")
                body = FoodEntryOnHandUpdate(entryId, omHand)
            }
        } catch (x: RedirectResponseException) {
            //ignored
        }
    }

    suspend fun updateShoppingListItemChecked(baseurl: String, accessToken: String, entry: TandoorShoppingListEntry) {
        try {
            getHttpClient().put<TandoorShoppingListEntry> {
                url("$baseurl/shopping-list-entry/${entry.id}/")
                contentType(ContentType.Application.Json)
                header("Authorization", "Token $accessToken")
                body = entry
            }
        } catch (x: RedirectResponseException) {
            //ignored
        }
    }
}
