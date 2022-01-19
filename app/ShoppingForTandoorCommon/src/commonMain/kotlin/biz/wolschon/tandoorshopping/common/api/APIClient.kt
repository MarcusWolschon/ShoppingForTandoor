package biz.wolschon.tandoorshopping.common.api

import biz.wolschon.tandoorshopping.common.Log
import biz.wolschon.tandoorshopping.common.api.model.TandoorPagedFoodList
import biz.wolschon.tandoorshopping.common.api.model.TandoorRecipe
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingList
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry
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

    suspend fun fetchMoreFoods(fullyUrl: String, accessToken: String) =
        getHttpClient().get<TandoorPagedFoodList> {
            url(fullyUrl)
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }

    suspend fun fetchRecipe(baseurl: String, accessToken: String, recipeId: Int) =
        getHttpClient().get<TandoorRecipe> {
            url("$baseurl/recipe/$recipeId")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }

    suspend fun fetchShoppingLists(baseurl: String, accessToken: String): List<TandoorShoppingList> {
        Log.i("APIClient", "fetchShoppingLists() HttpClient")
        val result =  getHttpClient().get<List<TandoorShoppingList>> {
            url("$baseurl/shopping-list/")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }
        Log.i("APIClient", "fetchShoppingLists() HttpClient returned")
        return result
    }

    @Serializable
    data class ShoppingListEntryUpdate(val id: Int, val checked: Boolean)

    suspend fun updateShoppingListItemChecked(baseurl: String, accessToken: String, entryId: Int, checked: Boolean) {
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
