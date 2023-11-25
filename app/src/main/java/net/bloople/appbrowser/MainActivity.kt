package net.bloople.appbrowser

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Toolbar


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

            else -> {}
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE_PREFERENCES && resultCode == RESULT_OK) {
            load()
        }
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

        webView.webViewClient = AppWebViewClient(this, browserContext)
        webView.webChromeClient = AppWebChromeClient(this, browserContext)
        webView.setInitialScale(1)
        webView.loadUrl(browserContext.baseUrl.toString())
    }

    companion object {
        private const val REQUEST_CODE_PREFERENCES = 0
    }
}