package biz.wolschon.tandoorshopping.common

import androidx.compose.runtime.Composable
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

expect fun getPlatformName(): String

expect class PlatformContext
@Composable
expect fun getPlatformContext():  PlatformContext

@Composable
expect fun addBackButtonHandler(handler: () -> Unit)

expect fun openBrowser(platformContext: PlatformContext, url: String)

expect val DBDispatcher : CoroutineDispatcher
expect val NetworkDispatcher : CoroutineDispatcher

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

expect fun getHttpClient(): HttpClient


expect val platformJson: Json