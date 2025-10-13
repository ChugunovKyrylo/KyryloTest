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

private const val GRID_ROUTE = "GRID"

private const val DETAILS_ROUTE = "DETAILS"

private const val ARG_GIF_MODEL = "gifModel"

@Composable
fun AppNavigation(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: () -> Unit,
    retryLoadingGridPage: Boolean
) {
    val controller = rememberNavController()
    NavHost(
        navController = controller,
        startDestination = GRID_ROUTE,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        composable(GRID_ROUTE) {
            GridScreen(
                onGifClicked = { gifModel ->
                    if (controller.currentDestination?.route == GRID_ROUTE) {
                        controller.currentBackStackEntry?.savedStateHandle?.set(
                            ARG_GIF_MODEL,
                            gifModel
                        )
                        controller.navigate(route = DETAILS_ROUTE)
                    }
                },
                onShowErrorPaging = onShowErrorSnackBar,
                retryLoadingGridPage = retryLoadingGridPage
            )
        }
        composable(DETAILS_ROUTE) {
            val gifModel =
                controller.previousBackStackEntry?.savedStateHandle?.get<GifModel>(ARG_GIF_MODEL)
            DetailScreen(gifModel) {
                if (controller.currentDestination?.route == DETAILS_ROUTE) {
                    controller.popBackStack()
                }
            }
        }
    }
}