package com.kyrylo.gifs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kyrylo.gifs.ui.detail.DetailScreen
import com.kyrylo.gifs.ui.grid.GridScreen

@Composable
fun AppNavigation() {
    val controller = rememberNavController()
    NavHost(
        navController = controller,
        startDestination = "grid"
    ) {
        composable("grid") {
            GridScreen { id ->
                if(controller.currentDestination?.route == "grid") {
                    controller.navigate(route = "details/${id}")
                }
            }
        }
        composable("details/{id}", arguments = listOf(
            navArgument("id") { type = NavType.StringType }
        )) {
            DetailScreen()
        }
    }
}