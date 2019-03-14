package com.jackchance.live360.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Objects

/**
 *  Created by lijiachang on 2019/3/14
 */
class JsonUtil {

    companion object {

        private val gson: Gson by lazy {
            Gson()
        }

        fun <T> jsonToObject(json: String, clsType: Class<T>): T {
            return gson.fromJson<T>(json, clsType)
        }

        fun <T> jsonToList(json: String, clsType: Class<T>): List<T> {
            return gson.fromJson(json, object : TypeToken<List<T>>() {}.type)
        }

        fun <T> toJson(objects: Objects): String {
            return gson.toJson(objects)
        }

        fun <T> toJson(list: List<T>): String {
            return gson.toJson(list)
        }

    }


}