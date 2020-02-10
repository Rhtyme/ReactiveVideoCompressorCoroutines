package com.rhtyme.reactivevideocompressor.data

import android.content.SharedPreferences

class PreferenceHelper(private val preferences: SharedPreferences)  {

    fun putString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, "")
    }

    fun getString(key: String, defValue: String): String {
        return preferences.getString(key, defValue) ?: defValue
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String): Int {
        return preferences.getInt(key, -1)
    }

    fun getInt(key: String, defVal: Int): Int {
        return preferences.getInt(key, defVal)
    }

    fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun getLong(key: String): Long {
        return preferences.getLong(key, -1)
    }

    fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    fun clear() {
        preferences.edit().clear().apply()
    }
}