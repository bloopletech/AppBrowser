@file:Suppress("DEPRECATION")

package net.bloople.appbrowser

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceGroup
import android.view.View


// Based on https://stackoverflow.com/a/18807490
@Suppress("OVERRIDE_DEPRECATION")
class AppBrowserPreferencesFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        for(i in 0 until preferenceScreen.preferenceCount) {
            val preference = preferenceScreen.getPreference(i)
            if(preference is PreferenceGroup) {
                for(j in 0 until preference.preferenceCount) {
                    val singlePref = preference.getPreference(j)
                    updatePreference(singlePref, singlePref.key)
                }
            }
            else {
                updatePreference(preference, preference.key)
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        updatePreference(findPreference(key), key)
    }

    private fun updatePreference(preference: Preference?, key: String?) {
        if(preference == null) return
        if(preference is ListPreference) {
            preference.summary = preference.entry
            return
        }
        if(preference is EditTextPreference) {
            preference.setSummary(preference.text)
            return
        }
        val sharedPrefs = preferenceManager.sharedPreferences
        preference.summary = sharedPrefs.getString(key, "Default")
    }
}