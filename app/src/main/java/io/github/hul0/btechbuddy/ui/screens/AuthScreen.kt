package io.github.hul0.btechbuddy.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AuthScreen(
    onTokensReceived: (String) -> Unit,
) {
    val appScheme = "btechbuddy"
    val redirectUrl = "callback"
    val authPageUrl = "https://btechbuddy.hulobiral.online/auth/login?app_scheme=$appScheme&redirect_url=$redirectUrl"

    val state = rememberWebViewState(url = authPageUrl)
    val navigator = rememberWebViewNavigator()
    var webViewError by remember { mutableStateOf(false) }

    // **Improvement 3: Seamless Back-Button Navigation**
    BackHandler(enabled = navigator.canGoBack) {
        navigator.navigateBack()
    }

    Scaffold(
        // **Improvement 4: An App Bar for Context**

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (webViewError) {
                // **Improvement 2: Graceful Error Handling**
                AuthErrorScreen(onRetry = {
                    webViewError = false
                    navigator.reload() // Or state.content = WebContent.Url(authPageUrl)
                })
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    // **Improvement 1: Enhanced Progress Indication**
                    val loadingState = state.loadingState
                    if (loadingState is LoadingState.Loading) {
                        LinearProgressIndicator(
                            progress = { loadingState.progress },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    WebView(
                        state = state,
                        modifier = Modifier.weight(1f),
                        navigator = navigator,
                        onCreated = { webView ->
                            webView.settings.javaScriptEnabled = true
                        },
                        client = object : AccompanistWebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val url = request?.url.toString()
                                if (url.startsWith("$appScheme://$redirectUrl")) {
                                    val fragment = request?.url?.fragment ?: ""
                                    if (fragment.isNotEmpty()) {
                                        onTokensReceived(fragment)
                                    }
                                    return true // We've handled the URL
                                }
                                return false // Let the WebView handle other URLs
                            }

                            override fun onReceivedError(
                                view: WebView,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                // Only show error for the main page request
                                if (request?.isForMainFrame == true) {
                                    webViewError = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * A simple, reusable error screen composable.
 */
@Composable
fun AuthErrorScreen(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Failed to load page",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Please check your internet connection and try again.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}