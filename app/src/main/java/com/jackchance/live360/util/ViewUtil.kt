package com.jackchance.live360.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.View
import android.widget.TextView


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

fun Float.dip2Pix(context: Context): Int{
    return UiUtil.dip2pix(context,this)
}

fun <T : View> Activity.viewById(@IdRes id: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE) { findViewById(id) as T }
}

fun <T : View> Fragment.viewById(@IdRes id: Int): Lazy<T?> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE) { view?.findViewById(id) as T? }
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
