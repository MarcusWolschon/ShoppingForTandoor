package biz.wolschon.tandoorshopping.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import biz.wolschon.tandoorshopping.common.model.Model
import biz.wolschon.tandoorshopping.common.page.*
import io.ktor.client.features.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private enum class TopLevelPages(val title: String,
                                 val page: Page,
                                 val icon: @Composable (() -> Painter)? = null) {
    LIST("Shopping List", ShoppingListPage(), icon_list),
    FOODS("Foods", FoodsPage(), icon_food),
    SHOPS("Shops", ShopsPage(), icon_shop),
    SETTINGS("Settings", SettingsPage(), icon_settings)
}

@Composable
fun App(model: Model, version: String) {
    // state
    val platformContext = getPlatformContext()
    val errorMessage = model.errorMessage.collectAsState()
    var isRefreshingMessage by remember { mutableStateOf<String?>(null) }
    var currentPage by remember { mutableStateOf(TopLevelPages.LIST.page) }
    addBackButtonHandler{
        currentPage.previous?.let {
            currentPage = it
        }
    }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // pages
    Log.d("App", "App recomposing currentPage=${currentPage.title}")

    val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("App", "NetworkDispatcher got", throwable)
        //if (throwable is UnresolvedAddressException) {
        //model.baseUrl = null
        //}
        if (throwable is ClientRequestException) {
            model.settings.apiToken = null
        }
    }

    val refresh = {
        isRefreshingMessage = "Refreshing..."
        GlobalScope.launch(NetworkDispatcher + errorHandler) {
            Log.i("App", "refresh starting")
            try {
                isRefreshingMessage = "Refreshing shopping list..."
                model.fetchShoppingList()
                if (model.errorMessage.value == null) {
                    isRefreshingMessage = "Refreshing foods..."
                    Log.i("App", "refresh - fetchFoods starting")
                    model.fetchFoods()
                    Log.i("App", "refresh done")
                }
                if (model.errorMessage.value == null) {
                    isRefreshingMessage = "Refreshing shops..."
                    Log.i("App", "refresh - fetchSupermarkets starting")
                    model.fetchSupermarkets()
                    Log.i("App", "refresh done")
                }
            } finally {
                Log.i("App", "refresh finally")
                isRefreshingMessage = null
            }
        }
    }

    if (model.settings.settingsIncomplete) {
        Log.i("App", "settings incomplete, forcing settings page")
        currentPage = TopLevelPages.SETTINGS.page
    } else if (model.databaseModel.getCachedShoppingListEntries().isEmpty() && errorMessage.value == null) {
        if (model.errorMessage.value == null && isRefreshingMessage == null) {
            Log.i("App", "initial refresh")
            refresh.invoke()
        }
    }



    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            // Drawer header

            Text("Shopping for Tandoor",
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, bottom=8.dp),
                fontSize = 16.sp
            )
            Text(version,
                modifier = Modifier.padding(start =16.dp, bottom=16.dp),
                fontSize = 12.sp
            )

            Divider()

            // Drawer items

            TopLevelPages.values().forEach { menuEntry ->
                Button(
                    enabled = currentPage != menuEntry.page,
                    modifier = Modifier.padding(16.dp).width(192.dp),
                    onClick = {
                        Log.i("App", "[${menuEntry.title}] tapped")
                        currentPage = menuEntry.page
                        scope.launch { scaffoldState.drawerState.close() }
                    }) {
                    menuEntry.icon?.let { icon ->
                        Icon(
                            painter = icon.invoke(),
                            contentDescription = menuEntry.title,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    Text(menuEntry.title)
                }
            }
        },
        floatingActionButton = {
            if (!model.settings.settingsIncomplete && isRefreshingMessage == null) {
                FloatingActionButton(
                    onClick = {
                        Log.i("App", "[refresh] tapped")
                        refresh.invoke()
                    }
                ) {
                    Icon(
                        painter = icon_refresh.invoke(),
                        contentDescription = "refresh",
                        modifier = Modifier.padding(5.dp)
                    )
                }/*
                ExtendedFloatingActionButton(
                    text = { Text("\uD83D\uDDD8 Refresh") },
                    icon = {
                        Icon(
                            painter = icon_refresh.invoke(),
                            contentDescription = "refresh",
                            modifier = Modifier.padding(5.dp)
                        )
                    },
                    onClick = {
                        Log.i("App", "[refresh] tapped")
                        refresh.invoke()
                    }

                )*/
            }
        }

    ) {


        Column {
            // top level bar
            Row {
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    Modifier.height(48.dp).width(48.dp)
                ) {
                    Icon(
                        painter = icon_menu.invoke(),
                        contentDescription = "open navigation drawer"
                    )
                }

                val message = isRefreshingMessage.takeIf { !it.isNullOrBlank() }
                if (message != null) {
                        Row(
                            Modifier
                                .background(Color.Yellow)
                                .fillMaxWidth()
                                .height(48.dp)
                                .weight(1f),
                        ) {
                            Text(
                                text = message,
                                color = Color.Black,
                                modifier = Modifier
                                    .align(CenterVertically)
                                    .padding(horizontal = 16.dp, vertical = 0.dp)
                            )
                        }
                    } else {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .weight(1f),
                        ) {
                            Text(
                                text = currentPage.title,
                                color = Color.Black,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .align(CenterVertically)
                                    .padding(horizontal = 16.dp, vertical = 0.dp)
                            )
                        }
                    }



                if (model.settings.baseUrl?.isNotBlank() == true) {
                    currentPage.relativeUrl?.let {
                        Button(
                            onClick = {
                                openBrowser(platformContext, model.settings.baseUrl + it)
                            },
                            Modifier.height(48.dp).width(48.dp).weight(0.2f)
                        ) {
                            Icon(
                                painter = icon_web.invoke(),
                                contentDescription = "open in web browser"
                            )
                        }
                    }
                }
            }

            // error messages below level bar

            errorMessage.value?.let { error ->
                Row(Modifier.background(MaterialTheme.colors.error).fillMaxWidth()) {
                    Text(text = error, color = MaterialTheme.colors.onError, maxLines = 3)
                }
            }

            // main content area
            //TODO Column(Modifier.verticalScroll(contentAreaScrollState, true)) {

            currentPage.compose(model, platformContext) { destination ->
                destination.previous = currentPage
                currentPage = destination
            }
        }
    }
}


