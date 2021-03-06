package biz.wolschon.tandoorshopping.common.page

import androidx.compose.runtime.*
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.view.shopListView

class ShopsPage: Page() {

    override val title = "All Supermarkets"

    override val relativeUrl = "/list/supermarket/"

    @Composable
    override fun compose(model: Model,
                         platformContext: PlatformContext,
                         navigateTo: (Page) -> Unit) {
        val allShops = model.databaseModel.getLiveSupermarkets()
            .collectAsState(initial = model.databaseModel.getCachedSupermarkets())
            .value

        shopListView(shops = allShops, showID = true) { shop ->
            navigateTo(ShopPage(shop))
        }
    }
}