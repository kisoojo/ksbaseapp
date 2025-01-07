package com.zenoation.ksbaseapp.kt.util

import android.content.Context
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.nio.charset.StandardCharsets

class JsonUtil private constructor() {

    fun <T> stringToObject(text: String?, type: Class<T>?): T? {
        val objectMapper = ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        try {
            return objectMapper.readValue(text, type)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }

        return null
    }

    fun <T> objectToString(`object`: T): String {
        try {
            val objectMapper = ObjectMapper()

            return objectMapper.writeValueAsString(`object`)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }

        return ""
    }

    fun <T> objectToJsonObject(`object`: T): JsonObject {
        val gson = Gson()
        val json = objectToString(`object`)
        return gson.fromJson(json, JsonObject::class.java)
    }

    fun <K, V> stringToMap(text: String?, keyClass: Class<K>?, valueClass: Class<V>?): Map<K, V>? {
        val objectMapper = ObjectMapper()
        try {
            val mapType =
                objectMapper.typeFactory.constructMapType(HashMap::class.java, keyClass, valueClass)
            return objectMapper.readValue(text, mapType)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
        return null
    }

    fun stringToJsonObject(json: String?): JsonObject {
        val gson = Gson()
        return gson.fromJson(json, JsonObject::class.java)
    }

    fun getAsStringNullable(jsonObject: JsonObject, key: String?): String? {
        val element = jsonObject[key]
        return if (element == null || element.isJsonNull) {
            null
        } else {
            jsonObject[key].asString
        }
    }

    fun stringToJsonArray(jsonArray: String?): JsonArray {
        val gson = Gson()
        return gson.fromJson(jsonArray, JsonArray::class.java)
    }

    fun contains(array: JsonArray, str: String): Boolean {
        for (i in 0 until array.size()) {
            if (array[i].asString == str) {
                return true
            }
        }
        return false
    }

    fun loadJsonFile(context: Context, fileName: String?): JsonObject? {
        var json: JsonObject? = null
        try {
            context.assets.open(fileName!!).use { `is` ->
                val size = `is`.available()
                val buffer = ByteArray(size)
                if (`is`.read(buffer) != -1) {
                    `is`.close()

                    json = stringToJsonObject(String(buffer, StandardCharsets.UTF_8))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return json
    }

    fun jsonArrayToStringArray(array: JsonArray): Array<String?>? {
        val stringArray = arrayOfNulls<String>(array.size())
        try {
            for (i in 0 until array.size()) {
                stringArray[i] = array[i].asString
            }
        } catch (e: Exception) {
            return null
        }

        return stringArray
    }

    companion object {
        val instance: JsonUtil by lazy { JsonUtil() }
    }
}
