package net.bloople.appbrowser

import android.webkit.WebChromeClient
import android.webkit.WebView

class AppWebChromeClient(private val activity: MainActivity, private val browserContext: BrowserContext) : WebChromeClient() {
    override fun onReceivedTitle(view: WebView, title: String) {
        activity.onTitleChanged(title)
    }
}