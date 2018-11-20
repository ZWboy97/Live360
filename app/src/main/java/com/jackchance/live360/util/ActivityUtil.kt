package com.jackchance.live360.util

import android.app.Activity
import android.content.Intent
import com.jackchance.live360.activity.MainActivity

/**
 *  Created by lijiachang on 2018/11/20
 */

fun Activity.toMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    this.startActivity(intent)
}
