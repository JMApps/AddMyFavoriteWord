package jmapps.addmyfavoriteword.presentation.ui.preferences

import android.content.SharedPreferences

data class SharedLocalProperties(val preferences: SharedPreferences) {
    companion object {
        private const val keyTaskInt_ = "key_task_int_"
        private const val keyTaskString_ = "key_task_string_"
        private const val keyTaskBoolean_ = "key_task_boolean_"
    }

    fun saveIntValue(key: String, value: Int) {
        preferences.edit().putInt("$keyTaskInt_$key", value).apply()
    }

    fun getIntValue(key: String, value: Int) : Int? {
        return preferences.getInt("$keyTaskInt_$key", value)
    }

    fun saveStringValue(key: String, value: String) {
        preferences.edit().putString("$keyTaskString_$key", value).apply()
    }

    fun getStringValue(key: String, value: String): String? {
        return preferences.getString("$keyTaskString_$key", value)
    }

    fun saveBooleanValue(key: String, value: Boolean) {
        preferences.edit().putBoolean("$keyTaskBoolean_$key", value).apply()
    }

    fun getBooleanValue(key: String, value: Boolean): Boolean? {
        return preferences.getBoolean("$keyTaskBoolean_$key", value)
    }
}
