package biz.wolschon.tandoorishopping.common

import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher

expect fun getPlatformName(): String

expect val DBDispatcher : CoroutineDispatcher
expect val NetworkDispatcher : CoroutineDispatcher

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

expect fun getHttpClient(): HttpClient
