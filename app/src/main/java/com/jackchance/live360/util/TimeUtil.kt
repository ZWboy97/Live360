package com.jackchance.live360.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    private const val MINUTE: Long = 60000L
    private const val HOUR: Long = 60 * MINUTE
    private const val DAY: Long = 24 * HOUR

    private const val FORMATE = "MM-dd HH:mm"
    private const val TODAY_FORMATE = "HH:mm"


    fun prettyTime(time: Long): String {
        val date = Date(time)
        val diff = Date().time - date.time
        var r = 0
        if (diff < MINUTE) {
            return "刚刚"
        }
        if (diff < 2 * MINUTE) {
            return "1分钟前"
        }
        if (diff < 50 * MINUTE) {
            return "${diff / MINUTE}分钟前"
        }
        if (diff < 90 * MINUTE) {
            return "一小时前"
        }
        if (isToday(time)) {
            val simpleDateFormat = SimpleDateFormat(TODAY_FORMATE)
            return simpleDateFormat.format(date)
        }
        val simpleDateFormat = SimpleDateFormat(FORMATE)
        return simpleDateFormat.format(date)
    }

    fun isToday(time: Long): Boolean {
        val pre = Calendar.getInstance()
        pre.time = Date(time)
        val cal = Calendar.getInstance()
        cal.time = Date(time)
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            val diffDay = cal.get(Calendar.DAY_OF_YEAR)
            -pre.get(Calendar.DAY_OF_YEAR);
            if (diffDay == 0) {
                return true
            }
        }
        return false
    }

}