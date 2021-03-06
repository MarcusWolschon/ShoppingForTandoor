package biz.wolschon.tandoorshopping.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import biz.wolschon.tandoorshopping.common.model.Model

@Preview
@Composable
fun DesktopApplicationPreview() {
    val modelState = remember { derivedStateOf { Model(DatabaseDriverFactory()) } }
    val model = modelState.value

    App(model, "preview")
}