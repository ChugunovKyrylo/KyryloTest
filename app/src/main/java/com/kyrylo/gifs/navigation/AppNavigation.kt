package com.kyrylo.gifs.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kyrylo.gifs.ui.detail.DetailScreen
import com.kyrylo.gifs.ui.grid.GridScreen
import com.kyrylo.gifs.ui.models.GifModel

@Composable
fun AppNavigation(paddingValues: PaddingValues) {
    val controller = rememberNavController()
    NavHost(
        navController = controller,
        startDestination = "grid",
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
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