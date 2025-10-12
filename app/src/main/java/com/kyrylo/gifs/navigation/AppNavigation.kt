package com.kyrylo.gifs.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kyrylo.gifs.presentation.detail.DetailScreen
import com.kyrylo.gifs.presentation.grid.GridScreen
import com.kyrylo.gifs.presentation.models.GifModel

@Composable
fun AppNavigation(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: () -> Unit,
    retryLoadingGridPage: Boolean
) {
    val controller = rememberNavController()
    NavHost(
        navController = controller,
        startDestination = "grid",
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        composable("grid") {
            GridScreen(
                onGifClicked = { gifModel ->
                    if (controller.currentDestination?.route == "grid") {
                        controller.currentBackStackEntry?.savedStateHandle?.set(
                            "gifModel",
                            gifModel
                        )
                        controller.navigate(route = "details")
                    }
                },
                onShowErrorPaging = onShowErrorSnackBar,
                retryLoadingGridPage = retryLoadingGridPage
            )
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