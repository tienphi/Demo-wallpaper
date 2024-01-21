package com.demo.wallpaper.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("main")

    data object Choose : Screen("choose-type")

    data object Images : Screen("images")

    data object Gifts : Screen("gifs")

    data object Detail : Screen(
        route = "detail/{type}/{url}",
        navArguments = listOf(
            navArgument("type") { type = NavType.StringType },
            navArgument("url") { type = NavType.StringType }
        )
    ) {
        fun createRoute(type: String, url: String): String =
            "detail/${type}/${URLEncoder.encode(url, StandardCharsets.UTF_8.toString())}"
    }
}
