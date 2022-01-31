package biz.wolschon.tandoorshopping.common.page

import androidx.compose.runtime.Composable
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.model.Model

abstract class Page {
    var previous: Page? = null
    abstract val title: String
    open val relativeUrl: String? = null

    @Composable
    abstract fun compose(model: Model, platformContext: PlatformContext, navigateTo: (Page) -> Unit)
}