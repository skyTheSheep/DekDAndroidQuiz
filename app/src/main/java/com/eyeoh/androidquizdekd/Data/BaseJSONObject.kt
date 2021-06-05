package com.eyeoh.androidquizdekd.Data

import org.json.JSONArray
import org.json.JSONObject

class BaseJSONObject : JSONObject {

    constructor() : super()
    constructor(str: String?) : super(str)


    fun getBoolean(name: String, defaultValue: Boolean): Boolean {
        try {
            return getBoolean(name)
        } catch (e: Exception) {
            return defaultValue
        }
    }

    fun getInt(name: String, defaultValue: Int): Int {
        try {
            return super.getInt(name)
        } catch (e: Exception) {
            return defaultValue
        }
    }

    fun getString(name: String, defaultValue: String): String {
        try {
            val str = super.getString(name)
            return if (str.toLowerCase().trim() == "null") return defaultValue else str
        } catch (e: Exception) {
            return defaultValue
        }
    }

    fun getLong(name: String, defaultValue: Long): Long {
        try {
            return super.getLong(name)
        } catch (e: Exception) {
            return defaultValue
        }
    }

    fun getDouble(name: String, defaultValue: Double): Double {
        try {
            return super.getDouble(name)
        } catch (e: Exception) {
            return defaultValue
        }
    }

    fun getJSONArray(name: String, defaultValue: JSONArray): JSONArray {
        try {
            return super.getJSONArray(name)
        } catch (e: Exception) {
            return defaultValue
        }
    }

    fun getJSONObject(name: String, defaultValue: JSONObject): JSONObject {
        try {
            return super.getJSONObject(name)
        } catch (e: Exception) {
            return defaultValue
        }
    }

    fun getBaseJSONObject(name: String): BaseJSONObject {
        try {
            return BaseJSONObject(super.getJSONObject(name).toString())
        } catch (e: Exception) {
            return BaseJSONObject()
        }
    }

    operator fun get(name: String, defaultValue: Any): Any {
        try {
            return super.get(name)
        } catch (e: Exception) {
            return defaultValue
        }
    }
}