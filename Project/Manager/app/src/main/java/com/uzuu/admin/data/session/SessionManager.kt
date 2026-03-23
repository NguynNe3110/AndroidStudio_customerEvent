package com.uzuu.admin.data.session

import android.content.Context
import android.content.SharedPreferences

/**
 * Singleton lưu token & username vào SharedPreferences.
 * Gọi SessionManager.init(context) một lần duy nhất trong MainActivity.onCreate().
 */
object SessionManager {

    private const val PREF_NAME    = "uzuu_admin_prefs"
    private const val KEY_TOKEN    = "auth_token"
    private const val KEY_USERNAME = "username"

    private lateinit var prefs: SharedPreferences

    /** Phải gọi trước khi dùng bất kỳ hàm nào khác */
    fun init(context: Context) {
        prefs = context.applicationContext
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun saveUsername(username: String) {
        prefs.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun clear() {
        prefs.edit().clear().apply()
    }
}