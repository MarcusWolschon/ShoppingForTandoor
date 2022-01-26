package biz.wolschon.tandoorshopping.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import biz.wolschon.tandoorshopping.common.model.db.AppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json


actual fun getPlatformName(): String {
    return "Android"
}

actual val DBDispatcher: CoroutineDispatcher = Dispatchers.IO
actual val NetworkDispatcher: CoroutineDispatcher = Dispatchers.IO

actual data class PlatformContext(val context: Context)

actual val platformJson = Json(KotlinxSerializer.DefaultJson) {
    isLenient = true
    ignoreUnknownKeys = true
}

@Composable
actual fun getPlatformContext() =  PlatformContext(
    context = LocalContext.current
)

actual fun openBrowser(platformContext: PlatformContext, url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    platformContext.context.startActivity(browserIntent)
}

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "offlinecache.db")
    }
}


actual fun getHttpClient(): HttpClient =
    HttpClient(CIO) {
        install(Auth) {
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(platformJson)
        }
        install(Logging) {
            level = LogLevel.HEADERS
        }
        BrowserUserAgent() // install default browser-like user-agent
        install(UserAgent) { agent = "Shopping For Tandoor Android app" }
    }