package io.github.hul0.btechbuddy.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AuthScreen(onTokensReceived: (String) -> Unit) {
    val appScheme = "btechbuddy"
    val redirectUrl = "callback"
    // IMPORTANT: Replace this with the actual URL of your deployed Vercel auth page
    val authPageUrl = "https://btechbuddy.hulobiral.online/auth/login?app_scheme=$appScheme&redirect_url=$redirectUrl"
    val state = rememberWebViewState(url = authPageUrl)
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        WebView(
            state = state,
            modifier = Modifier.fillMaxSize(),
            onCreated = { webView ->
                webView.settings.javaScriptEnabled = true
            },
            client = object : AccompanistWebViewClient() {
                override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    isLoading = true
                }

                override fun onPageFinished(view: WebView, url: String?) {
                    super.onPageFinished(view, url)
                    isLoading = false
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: android.webkit.WebResourceRequest?
                ): Boolean {
                    val url = request?.url.toString()
                    if (url.startsWith("btechbuddy://callback")) {
                        val fragment = request?.url?.fragment ?: ""
                        if (fragment.isNotEmpty()) {
                            onTokensReceived(fragment)
                        }
                        return true // We've handled the URL, don't load it in WebView
                    }
                    return false // Let the WebView handle other URLs
                }
            }
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
