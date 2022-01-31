@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
package biz.wolschon.tandoorshopping.common.model

import biz.wolschon.tandoorshopping.common.DatabaseDriverFactory
import biz.wolschon.tandoorshopping.common.api.APIClient
import biz.wolschon.tandoorshopping.common.Log
import biz.wolschon.tandoorshopping.common.api.model.*
import io.ktor.client.features.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.channels.UnresolvedAddressException

class Model(dbDriver: DatabaseDriverFactory) {
    companion object {
        const val defaultBaseURL = "https://rezepte.SERVER"
    }
    val databaseModel = DatabaseModel(dbDriver)
    val settings = databaseModel.createSettingsModel()

    private val api = APIClient()
    val errorMessage = MutableStateFlow<String?>(null)



    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Model", "Networking error: ", throwable)
        if (throwable is UnresolvedAddressException) {
            errorMessage.value = "Server address unresolvable"
        }
        if (throwable is ClientRequestException) {
            errorMessage.value = throwable.message
        }
        if (throwable is SerializationException) {
            errorMessage.value = "JSON malfunction"
        }
        /*if (throwable is kotlinx.serialization.MissingFieldException) {
            errorMessage.value = "API malfunction"
        }*/
    }

    private val errorJson = Json(KotlinxSerializer.DefaultJson) {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun fetchFoods(): List<TandoorFood>? {
        val apiUrl = settings.apiUrl ?: return null
        val apiToken = settings.apiToken ?: return null
        try {
            return coroutineScope {
                var response = api.fetchFoods(apiUrl, apiToken)
                val allFoods = response.results.toMutableList()
                while (response.next != null) {
                    response = api.fetchMoreFoods(response.next ?: break, apiToken)
                    allFoods.addAll(response.results)
                }

                databaseModel.saveFoods(allFoods)
                databaseModel.getCachedFoods()
            }
        } catch (x: UnresolvedAddressException) {
            Log.e("Model", "fetchFoods() error", x)
            errorMessage.value = "Server address unresolvable"
        } catch (x: ClientRequestException) {
            Log.e("Model", "fetchFoods() error", x)
            handleClientRequestException(x)
        }
        return null
    }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun fetchFood(id: TandoorFoodId): TandoorFood? {
        val apiUrl = settings.apiUrl ?: return null
        val apiToken = settings.apiToken ?: return null
        try {
            return coroutineScope {
                api.fetchFood(apiUrl, apiToken, id).also {
                    databaseModel.saveFood(it)
                }
            }
        } catch (x: UnresolvedAddressException) {
            Log.e("Model", "fetchFood() error", x)
            errorMessage.value = "Server address unresolvable"
        } catch (x: ClientRequestException) {
            Log.e("Model", "fetchFood() error", x)
            handleClientRequestException(x)
        }
        return null
    }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun fetchSupermarkets(): Map<TandoorSupermarketId, TandoorSupermarket>? {
        val apiUrl = settings.apiUrl ?: return null
        val apiToken = settings.apiToken ?: return null
        try {
            return coroutineScope {
                val allSupermarkets = api.fetchSupermarkets(apiUrl, apiToken)
                databaseModel.saveSupermarkets(allSupermarkets)
            }
        } catch (x: UnresolvedAddressException) {
            Log.e("Model", "fetchFoods() error", x)
            errorMessage.value = "Server address unresolvable"
        } catch (x: ClientRequestException) {
            Log.e("Model", "fetchFoods() error", x)
            handleClientRequestException(x)
        }
        return null
    }
/*
    suspend fun getAllSupermarkets(): Collection<TandoorSupermarket>? =
        cachedSupermarkets.takeIf { it.isNotEmpty() }?.toMap()?.values
            ?: fetchSupermarkets()?.values*/

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun handleClientRequestException(x: ClientRequestException) {
        val plainText = x.response.readText(Charsets.UTF_8)
        try {
            val message = errorJson.decodeFromString<TandoorError>(plainText)
            errorMessage.value = "Error: ${message.detail}"
        } catch (ex: Exception) {
            Log.e("Model", "Can't parse error response $plainText", ex)
            errorMessage.value = "Error: $plainText"
        }
    }

/*    private val cachedRecipes = mutableMapOf<Int, TandoorRecipe>()
    private suspend fun fetchRecipe(recipeId: RecipeId, cached: Boolean): TandoorRecipe? {
        if (cached) {
            cachedRecipes[recipeId]?.let { return it }
        }
        val apiUrl = settings.apiUrl ?: return null
        val apiToken = settings.apiToken ?: return null
        try {
            return coroutineScope {
                Log.e("Model", "fetchRecipe() calling api")
                api.fetchRecipe(apiUrl, apiToken, recipeId).also {
                    cachedRecipes[recipeId] = it
                }
            }
        } catch (x: UnresolvedAddressException) {
            Log.e("Model", "fetchRecipe() error", x)
            errorMessage.value = "Server address unresolvable"
        } catch (x: ClientRequestException) {
            Log.e("Model", "fetchRecipe() error", x)
            handleClientRequestException(x)
        }
        return null
    }*/

/*    private val cachedShoppingListRecipes = mutableMapOf<ShopppingListRecipeId, TandoorShoppingListRecipe>()
    private suspend fun fetchRecipeFromShoppingList(recipeId: ShopppingListRecipeId, cached: Boolean): TandoorRecipe? {
        if (cached) {
            cachedShoppingListRecipes[recipeId]?.let { return fetchRecipe(it.recipe, true) }
        }
        val apiUrl = settings.apiUrl ?: return null
        val apiToken = settings.apiToken ?: return null
        try {
            return coroutineScope {
                Log.e("Model", "fetchRecipeFromShoppingList() calling api")
                val shoppingListRecipe = api.fetchShoppingListRecipe(apiUrl, apiToken, recipeId).also {
                    cachedShoppingListRecipes[recipeId] = it
                }
                fetchRecipe(shoppingListRecipe.recipe, cached)
            }
        } catch (x: UnresolvedAddressException) {
            Log.e("Model", "fetchRecipeFromShoppingList() error", x)
            errorMessage.value = "Server address unresolvable"
        } catch (x: ClientRequestException) {
            Log.e("Model", "fetchRecipeFromShoppingList() error", x)
            handleClientRequestException(x)
        }
        return null
    }*/

    suspend fun fetchShoppingList(): List<TandoorShoppingListEntry>? {
        Log.e("Model", "fetchShoppingList() entered")
        val apiUrl = settings.apiUrl ?: return null
        val apiToken = settings.apiToken ?: return null
        Log.e("Model", "fetchShoppingList() starting")
        try {
            return coroutineScope {
                Log.e("Model", "fetchShoppingList() calling api")
                val allEntries = api.fetchShoppingList(apiUrl, apiToken)
                /*allEntries.forEach{ entry ->
                    entry.recipe?.let { recipe ->
                        //val recipe = fetchRecipeFromShoppingList(recipeId, true)
                        //entry.recipe = recipe
                        if (recipe == null) {
                            Log.e(
                                "Model", "Recipe $recipeId " +
                                        "for shopping list entry ${entry.id} = ${entry.food.name} " +
                                        "could not be loaded"
                            )
                        }
                    }
                }*/
                databaseModel.saveShoppingListEntries(allEntries)
                databaseModel.getCachedShoppingListEntries()
            }
        } catch (x: UnresolvedAddressException) {
            Log.e("Model", "fetchShoppingList() error", x)
            errorMessage.value = "Server address unresolvable"
        } catch (x: ClientRequestException) {
            Log.e("Model", "fetchShoppingList() error", x)
            handleClientRequestException(x)
        }
        return null
    }

    suspend fun updateShoppingListItemChecked(id: TandoorShoppingListEntryId, checked: Boolean) {
        val apiUrl = settings.apiUrl ?: return
        val apiToken = settings.apiToken ?: return
        return withContext(errorHandler) {
            coroutineScope {
                api.updateShoppingListItemChecked(apiUrl, apiToken, id, checked)
            }
        }
    }

    suspend fun updateFoodItemOnHand(id: TandoorFoodId, omHand: Boolean) {
        val apiUrl = settings.apiUrl ?: return
        val apiToken = settings.apiToken ?: return
        return withContext(errorHandler) {
            coroutineScope {
                api.updateFoodItemOnHand(apiUrl, apiToken, id, omHand)
            }
        }
    }

/*    suspend fun updateShoppingListItemChecked(entry: TandoorShoppingListEntry) {
        val apiUrl = settings.apiUrl ?: return
        val apiToken = settings.apiToken ?: return
        return withContext(errorHandler) {
            api.updateShoppingListItemChecked(apiUrl, apiToken, entry)
        }
    }*/
}