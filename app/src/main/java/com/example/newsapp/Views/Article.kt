package com.example.newsapp.Views

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import com.example.newsapp.components.ArticleLoader

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleScreen(url: String, title: String) {
    var isLoading by remember { mutableStateOf(true) }
    var startFadeOut by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startFadeOut) 0f else 1f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = LinearEasing
        ),
        finishedListener = {
            isLoading = false
        }, label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        setSupportZoom(true)
                    }
                    webViewClient = WebViewClient()
                }
            },
            update = { webView ->
                webView.loadUrl(url)
                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        view?.postDelayed({
                            startFadeOut = true
                        }, 1500)
                    }
                }
            }
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(alpha)
            ) {
                ArticleLoader(title = title)
            }
        }
    }
}
