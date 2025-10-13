package com.kyrylo.gifs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kyrylo.gifs.navigation.AppNavigation
import com.kyrylo.gifs.presentation.ui.theme.AppTheme
import com.kyrylo.gifs.presentation.ui.theme.errorContainerLight
import com.kyrylo.gifs.presentation.ui.theme.errorLight
import com.kyrylo.gifs.presentation.ui.theme.onErrorContainerLight
import com.kyrylo.gifs.presentation.ui.theme.onErrorLight
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
        val context = LocalContext.current
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
                        containerColor = errorContainerLight,
                        contentColor = onErrorContainerLight,
                        action = {
                            TextButton(
                                colors = ButtonColors(
                                    containerColor = errorLight,
                                    contentColor = onErrorLight,
                                    disabledContainerColor = errorLight,
                                    disabledContentColor = onErrorLight
                                ),
                                onClick = {
                                    Log.d("MainActivity", "retry clicked ${snackbarData.hashCode()}")
                                    snackbarData.dismiss()
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
                        retryLoadingGridPage = false
                        Log.d("MainActivity", "launch snackbar")
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = context.getString(R.string.gifs_were_not_loaded),
                                actionLabel = context.getString(R.string.retry),
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