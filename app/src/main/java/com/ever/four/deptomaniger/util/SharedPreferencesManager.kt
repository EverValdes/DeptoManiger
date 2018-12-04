package com.ever.four.deptomaniger.util

import android.content.Context

class SharedPreferencesManager
//Private constructor so we ensure we user the application context instead of and activity class context.
private constructor() {
    enum class Name(val value: String) {
        SHOPPER_NAME("SHOPPER_NAME")
    }
    private val SP_FILE = "com.ever.four.deptomaniger"
    private val cache: MutableMap<String, String> = mutableMapOf()

    init {
        /*
        *  every time init is called increment instance count
        *  just in case somehow we break singleton rule, this will be
        *  called more than once and myInstancesCount > 1 == true
        */
        ++myInstancesCount
    }


    companion object {
        //Debuggable field to check instance count
        var myInstancesCount = 0
        private lateinit var context: Context
        private val instance: SharedPreferencesManager = SharedPreferencesManager()

        @Synchronized
        fun getInstance(context: Context): SharedPreferencesManager {
            this.context = context
            return instance
        }
    }

    fun putName(fileName: String = SP_FILE, key: String = Name.SHOPPER_NAME.toString(), value: String) {
        var names: MutableSet<String> = getName(key = key)
        names.add(value)

        context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().putStringSet(key, names).apply()
    }

    fun getName(fileName: String = SP_FILE, key: String = Name.SHOPPER_NAME.toString()): MutableSet<String> {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getStringSet(key, HashSet<String>())
    }
}