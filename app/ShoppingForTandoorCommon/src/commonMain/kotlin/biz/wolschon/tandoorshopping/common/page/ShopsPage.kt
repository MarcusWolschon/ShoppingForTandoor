package biz.wolschon.tandoorshopping.common.page

import androidx.compose.runtime.*
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.view.shopListView

class ShopsPage: Page() {

    override val title = "All Supermarkets"

    @Composable
    override fun compose(model: Model,
                         platformContext: PlatformContext,
                         navigateTo: (Page) -> Unit) {
        val allShops = model.databaseModel.getCachedSupermarkets()

        shopListView(shops = allShops, showID = true) { shop ->
            navigateTo(ShopPage(shop))
        }
    }
}