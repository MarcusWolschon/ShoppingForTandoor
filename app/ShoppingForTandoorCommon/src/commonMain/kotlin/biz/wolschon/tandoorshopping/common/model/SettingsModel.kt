package biz.wolschon.tandoorshopping.common.model

import biz.wolschon.tandoorshopping.common.DBDispatcher
import biz.wolschon.tandoorshopping.common.Log
import biz.wolschon.tandoorshopping.common.api.model.TandoorSupermarketId
import biz.wolschon.tandoorshopping.common.model.db.SettingsQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsModel(private val settingsQueries: SettingsQueries) {

    val settingsIncomplete
        get() = apiToken.isNullOrBlank() || baseUrl == Model.defaultBaseURL

    //val baseUrlLive: Flow<String> = database.settingsQueries.getSetting("apiUrl").asFlow().mapToOneOrNull(context = DBDispatcher).map { it?.value ?: defaultBaseURL }
    var baseUrl: String?
        get() {
            val value = settingsQueries.getSetting("baseUrl").executeAsOneOrNull()?.value?.trim() ?: Model.defaultBaseURL
            Log.d("Model", "baseUrl.get $value")
            return value
        }
        set(value) {
            Log.d("Model", "baseUrl.set $value")
            settingsQueries.replaceSetting("baseUrl", value)
        }
    val apiUrl: String?
        get() = baseUrl?.let { "$it/api"  }

    val apiTokenLive: Flow<String> = settingsQueries.getSetting("apiToken")
        .asFlow()
        .mapToOneOrNull(context = DBDispatcher)
        .map { setting -> setting?.value ?: "" }

    var apiToken: String?
        get() = settingsQueries.getSetting("apiToken").executeAsOneOrNull()?.value
        set(value) = settingsQueries.replaceSetting("apiToken", value)

    var currentSupermarketID: TandoorSupermarketId?
        get() = settingsQueries.getSetting("currentSupermarketID").executeAsOneOrNull()?.value?.toInt()
        set(value) = settingsQueries.replaceSetting("currentSupermarketID", value.toString())

    var showCheckedShoppingEntries: Boolean?
        get() = settingsQueries.getSetting("shopping.checked-visible").executeAsOneOrNull()?.value?.toBoolean()
        set(value) = settingsQueries.replaceSetting("shopping.checked-visible", value.toString())

    var showOnlyOnHandFoods: Boolean?
        get() = settingsQueries.getSetting("foods.onHandOnly").executeAsOneOrNull()?.value?.toBoolean()
        set(value) = settingsQueries.replaceSetting("foods.onHandOnly", value.toString())
}