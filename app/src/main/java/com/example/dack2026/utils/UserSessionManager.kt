package com.example.dack2026.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.dack2026.data.entity.UserEntity

object UserSessionManager {
    private const val PREF_NAME = "user_session_pref"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"
    private const val KEY_FULL_NAME = "full_name"
    private const val KEY_PHONE = "phone"
    private const val KEY_ROLE = "role"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserSession(context: Context, user: UserEntity) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putLong(KEY_USER_ID, user.id)
        editor.putString(KEY_USERNAME, user.username)
        editor.putString(KEY_FULL_NAME, user.fullName)
        editor.putString(KEY_PHONE, user.phone)
        editor.putString(KEY_ROLE, user.role)
        editor.apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserRole(context: Context): String {
        return getPreferences(context).getString(KEY_ROLE, UserEntity.ROLE_CHU_TRO) ?: UserEntity.ROLE_CHU_TRO
    }

    fun getUsername(context: Context): String {
        return getPreferences(context).getString(KEY_USERNAME, "") ?: ""
    }

    fun getFullName(context: Context): String {
        return getPreferences(context).getString(KEY_FULL_NAME, "Người dùng") ?: "Người dùng"
    }

    fun getUserId(context: Context): Long {
        return getPreferences(context).getLong(KEY_USER_ID, -1L)
    }

    fun clearSession(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
}
