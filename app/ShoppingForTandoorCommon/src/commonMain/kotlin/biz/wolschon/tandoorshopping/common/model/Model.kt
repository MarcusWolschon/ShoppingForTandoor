package biz.wolschon.tandoorshopping.common.model

import biz.wolschon.tandoorshopping.common.model.db.AppDatabase
import biz.wolschon.tandoorshopping.common.DatabaseDriverFactory
import biz.wolschon.tandoorshopping.common.api.APIClient
import biz.wolschon.tandoorshopping.common.DBDispatcher
import biz.wolschon.tandoorshopping.common.api.model.TandoorFood
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingList
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Model(dbDriver: DatabaseDriverFactory) {
    companion object {
        const val defaultApiURL = "https://rezepte.SERVER/api"
    }

    val settingsIncomplete
        get() = apiToken.isNullOrBlank() || apiUrl == defaultApiURL
    private val api = APIClient()
    private val database = AppDatabase(
        dbDriver.createDriver()
        //XYZAdapter = XYZ.Adapter(OtherAdapters,...)
    )

    val apiUrlLive: Flow<String> = database.settingsQueries.getSetting("apiUrl").asFlow().mapToOneOrNull(context = DBDispatcher).map { it?.value ?: defaultApiURL }
    var apiUrl: String?
        get() = database.settingsQueries.getSetting("apiUrl").executeAsOneOrNull()?.value ?: defaultApiURL
        set(value) = database.settingsQueries.replaceSetting("apiUrl", value)

    val apiTokenLive: Flow<String> = database.settingsQueries.getSetting("apiToken").asFlow().mapToOneOrNull(context = DBDispatcher).map { it?.value ?: "" }
    var apiToken: String?
        get() = database.settingsQueries.getSetting("apiToken").executeAsOneOrNull()?.value
        set(value) = database.settingsQueries.replaceSetting("apiToken", value)

    suspend fun fetchFoods(): List<TandoorFood>? {
        var response = api.fetchFoods(apiUrl ?: return null, apiToken ?: return null)
        val allFoods = response.results.toMutableList()
        while (response.next != null) {
            response = api.fetchMoreFoods(response.next ?: break, apiToken ?: return null)
            allFoods.addAll(response.results)
        }

        return allFoods
    }
    suspend fun fetchShoppingLists(): List<TandoorShoppingList>? {
        return api.fetchShoppingLists(apiUrl ?: return null, apiToken ?: return null)
    }

    suspend fun updateShoppingListItemChecked(id: Int, checked: Boolean) {
        return api.updateShoppingListItemChecked(apiUrl ?: return, apiToken ?: return, id, checked)
    }

    suspend fun updateShoppingListItemChecked(entry: TandoorShoppingListEntry) {
        return api.updateShoppingListItemChecked(apiUrl ?: return, apiToken ?: return, entry)
    }
}