package biz.wolschon.tandoorishopping.android

import biz.wolschon.tandoorishopping.common.App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
//TODO use Jetpack Navigation import androidx.navigation.compose.rememberNavController
import biz.wolschon.tandoorishopping.common.DatabaseDriverFactory
import biz.wolschon.tandoorishopping.common.model.Model

class MainActivity : AppCompatActivity() {
    //@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val context = LocalContext.current
                val model = remember { derivedStateOf { Model(DatabaseDriverFactory(context)) } }
                //TODO use Jetpack Navigation val navController = rememberNavController()
                App(model.value)
            }
        }
    }
}