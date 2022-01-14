package biz.wolschon.tandoorishopping.common.model

import biz.wolschon.tandoorishopping.common.model.db.AppDatabase
import biz.wolschon.tandoorishopping.common.DatabaseDriverFactory
import biz.wolschon.tandoorishopping.common.api.APIClient
import biz.wolschon.tandoorishopping.common.DBDispatcher
import biz.wolschon.tandoorishopping.common.api.model.TandooriShoppingList
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Model(dbDriver: DatabaseDriverFactory) {
    companion object {
        const val defaultApiURL = "https://rezepte.SERVER/api"
    }

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
        get() = database.settingsQueries.getSetting("apiToken").executeAsOneOrNull()?.value ?: defaultApiURL
        set(value) = database.settingsQueries.replaceSetting("apiToken", value)

    suspend fun fetchShoppingLists(): Array<TandooriShoppingList>? {
        return api.fetchShoppingLists(apiUrl ?: return null, apiToken ?: return null)
    }
}