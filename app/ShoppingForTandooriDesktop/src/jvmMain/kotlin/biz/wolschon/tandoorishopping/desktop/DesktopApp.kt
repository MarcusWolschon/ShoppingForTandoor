package biz.wolschon.tandoorishopping.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import biz.wolschon.tandoorishopping.common.App
import biz.wolschon.tandoorishopping.common.DatabaseDriverFactory
import biz.wolschon.tandoorishopping.common.model.Model

@Preview
@Composable
//@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class, FlowPreview::class)
fun AppPreview() {
    val modelState = remember { derivedStateOf { Model(DatabaseDriverFactory()) } }
    val model = modelState.value

    App(model)
}