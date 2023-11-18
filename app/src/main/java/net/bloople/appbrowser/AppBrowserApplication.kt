@file: Suppress("DEPRECATION")

package net.bloople.appbrowser

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.preference.PreferenceManager

class AppBrowserApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        val preferences get() = PreferenceManager.getDefaultSharedPreferences(context)
    }
}