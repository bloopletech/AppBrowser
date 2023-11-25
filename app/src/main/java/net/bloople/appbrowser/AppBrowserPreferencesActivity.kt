@file:Suppress("DEPRECATION")

package net.bloople.appbrowser

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast

@Suppress("OVERRIDE_DEPRECATION")
class AppBrowserPreferencesActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_browser_preferences)

        setActionBar(findViewById(R.id.toolbar))

        findViewById<Button>(R.id.clear_cache).setOnClickListener {
            WebView(this).clearCache(true)
            Toast.makeText(this, "Cache Cleared", Toast.LENGTH_LONG).show()
        }

        fragmentManager.beginTransaction().replace(R.id.container, AppBrowserPreferencesFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                setResult(RESULT_OK)
                finish()
                return true
            }
        }

        return false
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
    }
}