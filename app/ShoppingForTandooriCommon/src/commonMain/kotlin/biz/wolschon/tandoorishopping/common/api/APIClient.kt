package biz.wolschon.tandoorishopping.common.api

import biz.wolschon.tandoorishopping.common.api.model.TandooriShoppingList
import io.ktor.client.request.*
//import io.ktor.client.statement.*
import io.ktor.http.*
import biz.wolschon.tandoorishopping.common.getHttpClient

class APIClient {

    suspend fun fetchShoppingLists(baseurl: String, accessToken: String) =
        getHttpClient().get<Array<TandooriShoppingList>> {
            url("$baseurl/shopping-list/")
            contentType(ContentType.Application.Json)
            header("Authorization", "Token $accessToken")
        }
}
