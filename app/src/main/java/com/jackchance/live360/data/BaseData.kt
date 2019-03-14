package com.jackchance.live360.data

import com.jackchance.live360.util.JsonUtil

/**
 *  Created by lijiachang on 2019/3/14
 */
open class BaseData {

    fun writeJson(): String {
        return JsonUtil.toJson(this)
    }

}