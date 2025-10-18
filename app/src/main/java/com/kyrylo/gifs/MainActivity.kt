package com.kyrylo.gifs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil3.imageLoader
import com.kyrylo.gifs.navigation.AppNavigation
import com.kyrylo.gifs.presentation.ui.theme.AppTheme
import com.kyrylo.gifs.presentation.ui.theme.errorContainerLight
import com.kyrylo.gifs.presentation.ui.theme.onErrorContainerLight
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onLowMemory() {
        super.onLowMemory()
        clearImageLoaderCache()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        clearImageLoaderCache()
    }

    private fun clearImageLoaderCache() {
        runCatching {
            imageLoader.memoryCache?.clear()
        }
    }
}

@Composable
private fun KyryloTestApp() {
    AppTheme {
        val context = LocalContext.current
        var snackBarShown by rememberSaveable {
            mutableStateOf(false)
        }
        var retryLoadingGridPage by rememberSaveable {
            mutableStateOf(false)
        }
        val snackBarHostState = remember { SnackbarHostState() }


        LaunchedEffect(snackBarShown) {
            if (snackBarShown) {
                retryLoadingGridPage = false
                Log.d("MainActivity", "launch snackbar")
                snackBarHostState.showSnackbar(
                    message = context.getString(R.string.gifs_were_not_loaded),
                    actionLabel = context.getString(R.string.retry),
                    duration = SnackbarDuration.Indefinite
                )
            }
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) { snackbarData ->
                    Snackbar(
                        containerColor = errorContainerLight,
                        contentColor = onErrorContainerLight,
                        action = {
                            TextButton(
                                colors = ButtonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError,
                                    disabledContainerColor = MaterialTheme.colorScheme.error,
                                    disabledContentColor = MaterialTheme.colorScheme.onError
                                ),
                                onClick = {
                                    Log.d(
                                        "MainActivity",
                                        "retry clicked ${snackbarData.hashCode()}"
                                    )
                                    snackBarShown = false
                                    snackbarData.dismiss()
                                    retryLoadingGridPage = true
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.retry),
                                )
                            }
                        },
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = snackbarData.visuals.message
                        )
                    }
                }
            }
        ) { paddingValues ->
            AppNavigation(
                paddingValues = paddingValues,
                onShowErrorSnackBar = {
                    if (snackBarShown.not()) {
                        snackBarShown = true
                    }
                },
                retryLoadingGridPage = retryLoadingGridPage
            )
        }
    }
}