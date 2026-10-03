package app.rooba.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rooba.ui.components.pressScale

private const val SIGNUP_URL = "https://accounts.firefox.com/signup"

/** Paths Mozilla's account site lands on once an account is created and confirmed. */
private val DONE_MARKERS = listOf("/settings", "connect_another_device", "signup_confirmed", "/pair")

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SignupScreen(
    onDone: () -> Unit,
    onBack: () -> Unit,
) {
    var loading by remember { mutableStateOf(true) }
    var finished by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }

    BackHandler {
        val wv = webView
        if (wv != null && wv.canGoBack() && !finished) wv.goBack() else onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create a Firefox account") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        bottomBar = {
            val interaction = remember { MutableInteractionSource() }
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                AnimatedVisibility(visible = finished, enter = fadeIn(tween(200)), exit = fadeOut(tween(150))) {
                    Text(
                        "Account ready. Sign in with the same email and password.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 10.dp),
                    )
                }
                if (finished) {
                    Button(
                        onClick = onDone,
                        interactionSource = interaction,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp).pressScale(interaction),
                    ) { Text("Sign in now", fontWeight = FontWeight.SemiBold) }
                } else {
                    OutlinedButton(
                        onClick = onDone,
                        interactionSource = interaction,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp).pressScale(interaction),
                    ) { Text("I already have an account") }
                }
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.setSupportMultipleWindows(false)
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                                loading = true
                                check(url)
                            }

                            override fun onPageFinished(view: WebView, url: String?) {
                                loading = false
                                check(url)
                            }

                            override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
                                check(url)
                            }

                            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                                // Keep everything on Mozilla's account pages inside the app.
                                val host = request.url.host ?: return true
                                return !(host.endsWith("firefox.com") || host.endsWith("mozilla.org") || host.endsWith("mozilla.com"))
                            }

                            private fun check(url: String?) {
                                if (url != null && DONE_MARKERS.any { url.contains(it) }) finished = true
                            }
                        }
                        loadUrl(SIGNUP_URL)
                        webView = this
                    }
                },
                onRelease = { it.destroy() },
            )
            if (loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
        }
    }
}
