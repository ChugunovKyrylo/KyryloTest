package com.kyrylo.gifs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kyrylo.gifs.navigation.AppNavigation
import com.kyrylo.gifs.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KyryloTestApp()
        }
    }
}

@Composable
private fun KyryloTestApp() {
    AppTheme {
        var snackBarShown by remember {
            mutableStateOf(false)
        }
        var retryLoadingGridPage by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        val snackBarHostState = remember { SnackbarHostState() }


        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) { snackbarData ->
                    Snackbar(
                        action = {
                            TextButton(
                                onClick = {
                                    Log.d("MainActivity", "retry clicked")
                                    snackbarData.dismiss()
                                }
                            ) {
                                Text(text = "Retry")
                            }
                        }
                    ) {
                        Text(snackbarData.visuals.message)
                    }
                }
            }
        ) { paddingValues ->
            AppNavigation(
                paddingValues = paddingValues,
                onShowErrorSnackBar = {
                    if (snackBarShown.not()) {
                        snackBarShown = true
                        retryLoadingGridPage = false
                        Log.d("MainActivity", "launch snackbar")
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Error paging",
                                actionLabel = "Retry",
                                duration = SnackbarDuration.Indefinite
                            )
                            retryLoadingGridPage = true
                            snackBarShown = false
                        }
                    }
                },
                retryLoadingGridPage = retryLoadingGridPage
            )
        }
    }
}