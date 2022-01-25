package biz.wolschon.tandoorshopping.common.page

import androidx.compose.runtime.*
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.api.model.TandoorSupermarket
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.view.shopDetailsView

class ShopPage(private val shop: TandoorSupermarket): Page() {

    override val title = shop.name

    @Composable
    override fun compose(model: Model,
                         platformContext: PlatformContext,
                         navigateTo: (Page) -> Unit) {
        shopDetailsView(shop)
    }
}