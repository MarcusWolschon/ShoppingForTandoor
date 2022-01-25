package biz.wolschon.tandoorshopping.common.page

import androidx.compose.runtime.Composable
import biz.wolschon.tandoorshopping.common.PlatformContext
import biz.wolschon.tandoorshopping.common.api.model.TandoorShoppingListEntry
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.view.SettingsView

class SettingsPage: Page() {
    override val title = "Settings"

    @Composable
    override fun compose(model: Model,
                         platformContext: PlatformContext,
                         navigateTo: (Page) -> Unit) {
        SettingsView(model.settings, model.errorMessage)
    }
}