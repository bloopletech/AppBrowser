package net.bloople.appbrowser

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Message
import android.webkit.HttpAuthHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebViewDatabase
import android.widget.EditText
import android.widget.TextView

class AppWebViewClient(private val activity: Activity, private val browserContext: BrowserContext) : WebViewClient() {
    private val webViewDatabase = WebViewDatabase.getInstance(activity)

    override fun onFormResubmission(view: WebView, dontResend: Message, resend: Message) {
        val builder = AlertDialog.Builder(activity)

        builder
            .setTitle("Confirm form resubmission")
            .setMessage("The page that you're looking for used information that you entered. Returning to that page " +
                "might cause any action you took to be repeated. Do you want to continue?")
            .setPositiveButton("Continue") { _, id ->
                resend.sendToTarget()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
                dontResend.sendToTarget()
            }
        builder.show()
    }

    override fun onReceivedHttpAuthRequest(view: WebView, handler: HttpAuthHandler, host: String, realm: String) {
        val credentials = webViewDatabase.getHttpAuthUsernamePassword(host, realm)

        if(credentials != null) handler.proceed(credentials[0], credentials[1])
        else promptForBasicAuthentication(handler, host, realm)
    }

    @SuppressLint("SetTextI18n")
    private fun promptForBasicAuthentication(handler: HttpAuthHandler, host: String, realm: String) {
        val builder = AlertDialog.Builder(activity)

        val dialogView = activity.layoutInflater.inflate(R.layout.dialog_basic_authentication, null)
        dialogView.findViewById<TextView>(R.id.host).text = "Host: $host"
        if(realm.isNotEmpty()) dialogView.findViewById<TextView>(R.id.realm).text = "Realm: $realm"

        builder
            .setView(dialogView)
            .setTitle("Sign in")
            .setPositiveButton("Sign In") { _, id ->
                val username = dialogView.findViewById<EditText>(R.id.username).text.toString()
                val password = dialogView.findViewById<EditText>(R.id.password).text.toString()
                webViewDatabase.setHttpAuthUsernamePassword(host, realm, username, password)

                handler.proceed(username, password)
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
                handler.cancel()
            }
        builder.show()
    }

    override fun shouldOverrideUrlLoading(view: WebView, webResourceRequest: WebResourceRequest): Boolean {
        val baseUrl = browserContext.baseUrl
        val url = webResourceRequest.url

        val isSameOrigin = baseUrl.scheme == url.scheme && baseUrl.host == url.host && baseUrl.port == url.port
        return !isSameOrigin
    }
}