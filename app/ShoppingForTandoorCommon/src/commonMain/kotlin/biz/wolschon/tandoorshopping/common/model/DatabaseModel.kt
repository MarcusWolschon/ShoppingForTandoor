@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
package biz.wolschon.tandoorshopping.common.model

import biz.wolschon.tandoorshopping.common.DatabaseDriverFactory
import biz.wolschon.tandoorshopping.common.api.model.*
import biz.wolschon.tandoorshopping.common.model.db.AppDatabase
import biz.wolschon.tandoorshopping.common.platformJson
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class DatabaseModel(dbDriver: DatabaseDriverFactory) {
    private val database = AppDatabase(
        dbDriver.createDriver()
        //XYZAdapter = XYZ.Adapter(OtherAdapters,...)
    )

    internal fun createSettingsModel() = SettingsModel(database.settingsQueries)

    ////////////////////////////////////////////////////////////////////////////
    // region ShoppingListEntries
    @OptIn(ExperimentalSerializationApi::class)
    private val cachedShoppingListEntries = mutableMapOf<TandoorShoppingListEntryId, TandoorShoppingListEntry>().also { cache ->
        database.shoppingListEntriesQueries.getAllShoppingListEntries().executeAsList().forEach {
            val market = platformJson.decodeFromString<TandoorShoppingListEntry>(it)
            cache[market.id] = market
        }
    }

    fun getCachedShoppingListEntries() = cachedShoppingListEntries.values.toList()

    @OptIn(ExperimentalSerializationApi::class)
    fun getLiveShoppingListEntries() = database.shoppingListEntriesQueries
        .getAllShoppingListEntries().asFlow()
        .mapToList(Dispatchers.IO)
        .map { list ->
            list.map { entry ->
                platformJson.decodeFromString<TandoorShoppingListEntry>(entry)
            }
        }

    @OptIn(ExperimentalSerializationApi::class)
    fun saveShoppingListEntries(list: Collection<TandoorShoppingListEntry>): Map<TandoorShoppingListEntryId, TandoorShoppingListEntry>  {
        cachedShoppingListEntries.clear()
        list.forEach { cachedShoppingListEntries[it.id] = it }
        database.shoppingListEntriesQueries.deleteAllShoppingListEntries()
        list.forEach {
            database.shoppingListEntriesQueries.insertShoppingListEntry(
                it.id.toLong(),
                it.food.name,
                platformJson.encodeToString(it)
            )
        }

        return cachedShoppingListEntries.toMap()
    }
    // endregion

    ////////////////////////////////////////////////////////////////////////////
    // region supermarkets
    @OptIn(ExperimentalSerializationApi::class)
    private val cachedSupermarkets = mutableMapOf<TandoorSupermarketId, TandoorSupermarket>().also { cache ->
        database.supermarketsQueries.getAllSupermarkets().executeAsList().forEach {
            val market = platformJson.decodeFromString<TandoorSupermarket>(it)
            cache[market.id] = market
        }
    }

    fun getCachedSupermarkets() = cachedSupermarkets.values.toList()

    @OptIn(ExperimentalSerializationApi::class)
    fun getLiveSupermarkets() = database.supermarketsQueries
        .getAllSupermarkets().asFlow()
        .mapToList(Dispatchers.IO)
        .map { list ->
            list.map { entry ->
                platformJson.decodeFromString<TandoorSupermarket>(entry)
            }
        }

    @OptIn(ExperimentalSerializationApi::class)
    fun saveSupermarkets(list: Collection<TandoorSupermarket>): Map<TandoorSupermarketId, TandoorSupermarket>  {
        cachedSupermarkets.clear()
        list.forEach { cachedSupermarkets[it.id] = it }
        database.supermarketsQueries.deleteAllSupermarkets()
        list.forEach {
            database.supermarketsQueries.insertSupermarket(
                it.id.toLong(),
                it.name,
                platformJson.encodeToString(it)
            )
        }

        return cachedSupermarkets.toMap()
    }
    // endregion

    ////////////////////////////////////////////////////////////////////////////
    // region foods
    @OptIn(ExperimentalSerializationApi::class)
    private val cachedFoods = mutableMapOf<TandoorFoodId, TandoorFood>().also { cache ->
        database.foodsQueries.getAllFoods().executeAsList().forEach {
            val market = platformJson.decodeFromString<TandoorFood>(it)
            cache[market.id] = market
        }
    }

    fun getCachedFoods() = cachedFoods.values.toList()

    @OptIn(ExperimentalSerializationApi::class)
    fun getLiveFoods() = database.foodsQueries
        .getAllFoods().asFlow()
        .mapToList(Dispatchers.IO)
        .map { list ->
            list.map { entry ->
                platformJson.decodeFromString<TandoorFood>(entry)
            }
        }

    @OptIn(ExperimentalSerializationApi::class)
    fun getLiveFood(id: TandoorFoodId): Flow<TandoorFood?> = database.foodsQueries
        .getFood(id.toLong()).asFlow()
        .mapToOneOrNull(Dispatchers.IO)
        .map { entry ->
            entry?.let { platformJson.decodeFromString(entry) }
        }

    @OptIn(ExperimentalSerializationApi::class)
    fun saveFoods(list: Collection<TandoorFood>) {
        cachedFoods.clear()
        list.forEach { cachedFoods[it.id] = it }
        database.foodsQueries.deleteAllFoods()
        list.forEach {
            database.foodsQueries.insertFood(
                it.id.toLong(),
                it.name,
                platformJson.encodeToString(it)
            )
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun saveFood(food: TandoorFood) {
        cachedFoods[food.id] = food
        database.foodsQueries.replaceFood(
            id = food.id.toLong(),
            name = food.name,
            json = platformJson.encodeToString(food)
        )
    }
    // endregion
}