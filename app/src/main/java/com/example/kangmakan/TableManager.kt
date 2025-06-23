package com.example.kangmakan

import android.content.Context

object TableManager {
    private const val PREFS_NAME = "kang_pesen_prefs"
    private const val KEY_SELECTED_TABLE = "selected_table"

    fun saveSelectedTableNumber(context: Context, tableNumber: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_SELECTED_TABLE, tableNumber)
            apply()
        }
    }

    fun getSelectedTableNumber(context: Context): String {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_SELECTED_TABLE, "") ?: ""
    }

    fun clearSelectedTable(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove(KEY_SELECTED_TABLE)
            apply()
        }
    }

    fun getTableNumberOnly(context: Context): Int? {
        val fullTableName = getSelectedTableNumber(context)
        return if (fullTableName.startsWith("Meja ")) {
            fullTableName.removePrefix("Meja ").toIntOrNull()
        } else {
            null
        }
    }
}