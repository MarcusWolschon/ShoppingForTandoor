package biz.wolschon.tandoorshopping.common

import biz.wolschon.tandoorshopping.common.model.db.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
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
    return "Desktop"
}

actual val DBDispatcher: CoroutineDispatcher = Dispatchers.IO
actual val NetworkDispatcher: CoroutineDispatcher = Dispatchers.IO

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
            AppDatabase.Schema.create(it)
        } //TODO: for testing
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
        install(UserAgent) { agent = "Shopping For Tandoori Desktop app" }
    }