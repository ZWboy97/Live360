package com.jackchance.live360.util

import android.content.Context

/**
 *  Created by lijiachang on 2018/11/20
 */

object UiUtil {

    fun dip2pix(context: Context, dip: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }

}