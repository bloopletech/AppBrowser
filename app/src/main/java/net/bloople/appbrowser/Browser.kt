package net.bloople.appbrowser

import android.graphics.Bitmap

interface Browser {
    fun onReceivedIcon(icon: Bitmap?)
    fun onTitleChanged(title: String)
}