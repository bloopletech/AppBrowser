package net.bloople.appbrowser

import android.annotation.SuppressLint
import android.app.Activity
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
        toolbar.navigationIcon = resources.getDrawable(R.drawable.baseline_arrow_back_24, null)
        toolbar.setNavigationOnClickListener {
            if(webView.canGoBack()) webView.goBack()
        }

        setActionBar(toolbar)

        val browserContext = BrowserContext(Uri.parse("https://192.168.1.100:9292/"))
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
            R.id.refresh -> webView.reload()
            else -> {}
        }

        return true
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

        webView.webViewClient = AppWebViewClient(this, browserContext)
        webView.webChromeClient = AppWebChromeClient(this, browserContext)
        webView.loadUrl(browserContext.baseUrl.toString())
    }
}