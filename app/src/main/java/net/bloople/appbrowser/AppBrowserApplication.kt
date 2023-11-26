@file: Suppress("DEPRECATION")

package net.bloople.appbrowser

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.preference.PreferenceManager


class AppBrowserApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        var isDebuggable: Boolean = false

        val preferences get() = PreferenceManager.getDefaultSharedPreferences(context)
    }
}