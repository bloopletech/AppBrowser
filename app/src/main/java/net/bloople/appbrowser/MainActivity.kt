package net.bloople.appbrowser

import android.R.attr.bitmap
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Toolbar
import kotlin.math.roundToInt


class MainActivity : Activity(), Browser {
    private lateinit var toolbar: Toolbar
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)

        toolbar = findViewById(R.id.toolbar)
        setActionBar(toolbar)

        load()
    }

    private fun load() {
        val baseUrl = AppBrowserApplication.preferences.getString("baseUrl", null) ?: "about:blank"
        val browserContext = BrowserContext(Uri.parse(baseUrl))
        configureWebView(webView, browserContext)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.browser_menu, menu)
        if(AppBrowserApplication.isDebuggable) menuInflater.inflate(R.menu.browser_debug_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            android.R.id.home -> webView.goBack()
            R.id.forwards -> webView.goForward()
            R.id.refresh -> webView.reload()
            R.id.manage_preferences -> {
                val intent = Intent(this@MainActivity, AppBrowserPreferencesActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_PREFERENCES)
            }
            R.id.recreate_activity -> recreate()

            else -> {}
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE_PREFERENCES && resultCode == RESULT_OK) {
            load()
        }
    }

    override fun onReceivedIcon(icon: Bitmap?) {
        toolbar.logo = if(icon != null) {
            val targetSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32.0f, resources.displayMetrics)
            val scale = (targetSize / icon.width).coerceAtMost(targetSize / icon.height)
            BitmapDrawable(
                resources,
                Bitmap.createScaledBitmap(
                    icon,
                    (icon.width * scale).roundToInt(),
                    (icon.height * scale).roundToInt(), true)
            )
        }
        else null
    }

    override fun onTitleChanged(title: String) {
        toolbar.title = title
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView(webView: WebView, browserContext: BrowserContext) {
        val webSettings = webView.settings
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        //setJavaScriptCanOpenWindowsAutomatically
        webSettings.javaScriptEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        //setSupportMultipleWindows
        webSettings.setSupportZoom(false)
        webSettings.builtInZoomControls = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true

        webView.webViewClient = AppWebViewClient(this, this, browserContext)
        webView.webChromeClient = AppWebChromeClient(this, this, browserContext)
        webView.setInitialScale(1)
        webView.loadUrl(browserContext.baseUrl.toString())
    }

    companion object {
        private const val REQUEST_CODE_PREFERENCES = 0
    }
}