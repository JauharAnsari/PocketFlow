package com.example.pocketflow.data

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("pocket_flow_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        private const val KEY_PROFILE_NAME = "profile_name"
        private const val KEY_PROFILE_AVATAR_INDEX = "profile_avatar_index"
    }

    var isBiometricEnabled: Boolean
        get() = prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, value).apply()

    var profileName: String
        get() = prefs.getString(KEY_PROFILE_NAME, "John Doe") ?: "John Doe"
        set(value) = prefs.edit().putString(KEY_PROFILE_NAME, value).apply()

    var profileAvatarIndex: Int
        get() = prefs.getInt(KEY_PROFILE_AVATAR_INDEX, 0)
        set(value) = prefs.edit().putInt(KEY_PROFILE_AVATAR_INDEX, value).apply()
}
