package biz.wolschon.tandoorshopping.common.page

import androidx.compose.runtime.Composable
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry
import biz.wolschon.tandoorshopping.common.model.Model

abstract class Page {
    abstract val title: String

    @Composable
    abstract fun compose(model: Model, platformContext: PlatformContext, navigateTo: (Page) -> Unit)
}