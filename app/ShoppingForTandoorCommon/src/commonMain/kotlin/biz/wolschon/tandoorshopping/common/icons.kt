package biz.wolschon.tandoorshopping.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

expect val icon_list: @Composable () -> Painter
expect val icon_shop: @Composable () -> Painter
expect val icon_food: @Composable () -> Painter
expect val icon_settings: @Composable () -> Painter
expect val icon_web: @Composable () -> Painter
expect val icon_menu: @Composable () -> Painter
