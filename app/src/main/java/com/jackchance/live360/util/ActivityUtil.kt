package com.jackchance.live360.util

import android.app.Activity
import android.content.Intent
import com.jackchance.live360.activity.HomeActivity
import com.jackchance.live360.activity.MainActivity
import com.jackchance.live360.activity.TestVideoActivity
import com.jackchance.live360.activity.VideoPlayerActivity

/**
 *  Created by lijiachang on 2018/11/20
 */

fun Activity.toMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    this.startActivity(intent)
}

fun Activity.toHomeActivityt() {
    val intent = Intent(this,HomeActivity::class.java)
    this.startActivity(intent)
}

fun Activity.toLiveActivity(url: String, isVRPlay: Boolean){
    if (isVRPlay) {
        val intent = Intent(this, TestVideoActivity::class.java)
        intent.putExtra("path", url)
        startActivity(intent)
    } else {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("path", url)
        startActivity(intent)
    }
}