package biz.wolschon.tandoorshopping.common

import android.content.Context
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
            serializer = KotlinxSerializer(Json(KotlinxSerializer.DefaultJson) {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.HEADERS
        }
        BrowserUserAgent() // install default browser-like user-agent
        install(UserAgent) { agent = "Shopping For Tandoor Android app" }
    }