package net.bloople.appbrowser

import android.app.Activity
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView

class AppWebChromeClient(
    private val activity: Activity,
    private val browser: Browser,
    private val browserContext: BrowserContext) : WebChromeClient() {
    override fun onReceivedIcon(view: WebView, icon: Bitmap) {
        browser.onReceivedIcon(icon)
    }

    override fun onReceivedTitle(view: WebView, title: String) {
        browser.onTitleChanged(title)
    }
}