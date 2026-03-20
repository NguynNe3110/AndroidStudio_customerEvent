package com.uzuu.customer.data.session

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "app_prefs"
    private const val KEY_TOKEN    = "access_token"
    private const val KEY_USERNAME = "username"
    private const val KEY_AVATAR   = "avatar_uri"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ── Token ────────────────────────────────────────────────────────────────
    fun saveToken(token: String) = prefs.edit().putString(KEY_TOKEN, token).apply()
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    // ── Username ─────────────────────────────────────────────────────────────
    fun saveUsername(username: String) = prefs.edit().putString(KEY_USERNAME, username).apply()
    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    // ── Avatar (local URI string) ─────────────────────────────────────────────
    fun saveAvatarUri(uri: String) = prefs.edit().putString(KEY_AVATAR, uri).apply()
    fun getAvatarUri(): String? = prefs.getString(KEY_AVATAR, null)

    // ── Clear all ────────────────────────────────────────────────────────────
    fun clear() {
        prefs.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_USERNAME)
            // Avatar sẽ giữ lại (tùy ý)
            .apply()
    }
}