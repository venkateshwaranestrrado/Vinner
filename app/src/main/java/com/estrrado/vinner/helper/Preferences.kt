package com.estrrado.vinner.helper

import android.app.Service
import android.content.Context
import androidx.fragment.app.FragmentActivity

object Preferences {

    const val PREFERENCE_NAME = "DATA"
    const val TOKEN = "id"

    fun clearOne(context: Context?, FileName: String, KeyName: String) {
        val settings = context?.getSharedPreferences(FileName, Context.MODE_PRIVATE)
        settings?.edit()?.remove(KeyName)?.apply()
    }

    fun getAll(context: Context, FileName: String): String? {
        val preferencesData = context.applicationContext.getSharedPreferences(FileName, Context.MODE_PRIVATE)
        return preferencesData.getString("keyName", "keyValue")
    }

    fun clear(context: Context, FileName: String) {
        val prefs = context.getSharedPreferences(FileName, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun get(context: FragmentActivity?, KeyName: String): String? {
        val preferencesData = context?.applicationContext?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferencesData?.getString(KeyName, "")
    }

    fun get(context: Service?, KeyName: String): String? {
        val preferencesData = context?.applicationContext?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferencesData?.getString(KeyName, "")
    }

    fun put(context: FragmentActivity?, key: String, value: String) {
        val sharedPreferences = context?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val spe = sharedPreferences?.edit()
        spe?.putString(key, value)
        spe?.apply()
    }

    fun put(context: Service?, key: String, value: String) {
        val sharedPreferences = context?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val spe = sharedPreferences?.edit()
        spe?.putString(key, value)
        spe?.apply()
    }
}