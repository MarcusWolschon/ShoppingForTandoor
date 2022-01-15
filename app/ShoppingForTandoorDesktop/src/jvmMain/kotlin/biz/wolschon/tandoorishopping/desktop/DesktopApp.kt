package biz.wolschon.tandoorshopping.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import biz.wolschon.tandoorshopping.common.App
import biz.wolschon.tandoorshopping.common.DatabaseDriverFactory
import biz.wolschon.tandoorshopping.common.model.Model

@Preview
@Composable
//@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class, FlowPreview::class)
fun AppPreview() {
    val modelState = remember { derivedStateOf { Model(DatabaseDriverFactory()) } }
    val model = modelState.value

    App(model)
}