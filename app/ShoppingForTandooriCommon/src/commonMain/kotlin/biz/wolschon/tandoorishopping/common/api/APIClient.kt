package biz.wolschon.tandoorishopping.common.api

import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingList
import biz.wolschon.tandoorishopping.common.api.model.TandoorShoppingListEntry
import io.ktor.client.request.*
import io.ktor.http.*
import biz.wolschon.tandoorishopping.common.getHttpClient
import io.ktor.client.features.*
import kotlinx.serialization.Serializable

class APIClient {

    suspend fun fetchShoppingLists(baseurl: String, accessToken: String) =
        getHttpClient().get<List<TandoorShoppingList>> {
            url("$baseurl/shopping-list/")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
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
