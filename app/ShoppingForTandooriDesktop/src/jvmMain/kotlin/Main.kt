import biz.wolschon.tandoorishopping.common.App
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import biz.wolschon.tandoorishopping.common.DatabaseDriverFactory
import biz.wolschon.tandoorishopping.common.model.Model

fun main() = application {
    val state = rememberWindowState(
        placement = WindowPlacement.Maximized,
        width = Dp.Unspecified,
        height = Dp.Unspecified
    )
    Window(
        title = "Shopping",
        onCloseRequest = ::exitApplication,
        state = state,
        resizable = true//,
        //icon = painterResource("favicon.ico")
    ) {
        MaterialTheme {
            val modelState = remember { derivedStateOf { Model(DatabaseDriverFactory()) } }
            val model = modelState.value

            App(model)
        }
    }
}