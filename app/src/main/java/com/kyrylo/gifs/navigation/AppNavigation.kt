package com.kyrylo.gifs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kyrylo.gifs.ui.detail.DetailScreen
import com.kyrylo.gifs.ui.grid.GridScreen
import com.kyrylo.gifs.ui.models.GifModel

@Composable
fun AppNavigation() {
    val controller = rememberNavController()
    NavHost(
        navController = controller,
        startDestination = "grid"
    ) {
        composable("grid") {
            GridScreen { gifModel ->
                if (controller.currentDestination?.route == "grid") {
                    controller.currentBackStackEntry?.savedStateHandle?.set("gifModel", gifModel)
                    controller.navigate(route = "details")
                }
            }
        }
        composable("details") {
            val gifModel =
                controller.previousBackStackEntry?.savedStateHandle?.get<GifModel>("gifModel")
            DetailScreen(gifModel) {
                controller.popBackStack()
            }
        }
    }
}