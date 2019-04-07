package com.jackchance.live360.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jackchance.live360.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


/**
 *  Created by lijiachang on 2018/11/20
 */

interface ViewHolder {

    val view: View
}

/**kotlin 属性扩展*/
inline var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

inline var TextView.textColorRes: Int
    get() = throw Exception()
    set(@IdRes value) {
        @Suppress("DEPRECATION")
        setTextColor(context?.resources?.getColor(value) ?: return)
    }

inline var TextView.textSizeDp: Int
    get() = throw Exception()
    set(value) {
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, value.toFloat())
    }

/**kotlin 函数扩展**/
fun Int.dip2Pix(context: Context): Int {
    return UiUtil.dip2pix(context, this.toFloat())
}

fun Float.dip2Pix(context: Context): Int {
    return UiUtil.dip2pix(context, this)
}

fun <T : View> Activity.viewById(@IdRes id: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE) { findViewById(id) as T }
}

fun <T : View> Fragment.viewById(@IdRes id: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE) { view?.findViewById(id) as T }
}

fun <T : View> DialogFragment.viewById(@IdRes id: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE) { dialog?.findViewById(id) as T }
}

fun <T : View> Dialog.viewById(@IdRes id: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE) { findViewById(id) as T }
}

fun <T : View> View.viewById(@IdRes id: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE) { findViewById(id) as T }
}

fun <T : View> ViewHolder.viewById(@IdRes id: Int): Lazy<T> = view.viewById(id)

/**
 * Picasso 图片加载扩展
 */
fun ImageView.loadUrl(url: String) {
    if (url.isNullOrEmpty()) {
        return
    }
    Picasso.get()
            .load(url)
            .placeholder(R.drawable.ic_empty_zhihu)
            .error(R.drawable.ic_empty_zhihu)
            .into(this)

}

fun Long.toMMddHHmm(): String {
    val formater = SimpleDateFormat("MM-dd HH:mm")
    return formater.format(Date(this))
}

/**
 * 时间格式化工具
 */
//fun Long.toPrettyString(){
//    val DATETIME_FORMATTER = SimpleDateFormat("yyyy-MM-dd HH:mm")
//    val current = System.currentTimeMillis()
//    val diff = current - this
//    return if (diff < 0L) {
//        DATETIME_FORMATTER.format(Date(this))
//    } else if (diff < 60000L) {
//        diff / 1000L + "秒前"
//    } else if (diff < 3600000L) {
//        diff / 60000L + "分钟前"
//    } else if (isToday) {
//        "今天 " + TIME_FORMATTER.format(Date(time))
//    } else {
//        if (isYesterday(time)) "昨天 " + TIME_FORMATTER.format(Date(time)) else DATETIME_FORMATTER.format(Date(time))
//    }
//
//}
//
//fun Long.isFuture(): Boolean{
//    val current = System.currentTimeMillis()
//    return current - this < 0L
//}
//
//fun Long.isToday(): Boolean{
//    val diff = this - startOfToday()
//    return diff >= 0L && diff < 86400000L
//}