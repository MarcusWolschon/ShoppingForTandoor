package biz.wolschon.tandoorshopping.android

import biz.wolschon.tandoorshopping.common.App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import biz.wolschon.tandoorshopping.common.DarkThemeColors
//TODO use Jetpack Navigation import androidx.navigation.compose.rememberNavController
import biz.wolschon.tandoorshopping.common.DatabaseDriverFactory
import biz.wolschon.tandoorshopping.common.LightThemeColors
import biz.wolschon.tandoorshopping.common.model.Model

class MainActivity : AppCompatActivity() {
    //@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) DarkThemeColors else  LightThemeColors) {
                val context = LocalContext.current
                val model = remember { derivedStateOf { Model(DatabaseDriverFactory(context)) } }
                //TODO use Jetpack Navigation val navController = rememberNavController()
                App(model.value, BuildConfig.VERSION_NAME)
            }
        }
    }
}