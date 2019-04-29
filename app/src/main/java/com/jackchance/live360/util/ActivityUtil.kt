package com.jackchance.live360.util

import android.app.Activity
import android.content.Intent
import com.jackchance.live360.activity.HomeActivity
import com.jackchance.live360.activity.TestVideoActivity
import com.jackchance.live360.activity.VideoPlayerActivity
import com.jackchance.live360.data.LiveRoom

/**
 *  Created by lijiachang on 2018/11/20
 */

fun Activity.toHomeActivityt() {
    val intent = Intent(this, HomeActivity::class.java)
    this.startActivity(intent)
}

fun Activity.toLiveActivity(url: String, isVRPlay: Boolean) {
    if (isVRPlay) {
        val intent = Intent(this, TestVideoActivity::class.java)
        intent.putExtra("path", url)
        intent.putExtra("is_living", false)
        startActivity(intent)
    } else {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("path", url)
        intent.putExtra("is_living", false)
        startActivity(intent)
    }
}

fun Activity.toLiveActivity(liveRoom: LiveRoom) {
    liveRoom.isVR = true
    if (liveRoom.isVR) {
        val intent = Intent(this, TestVideoActivity::class.java)
        intent.putExtra("path", liveRoom.pullRTMPUrl)
        intent.putExtra("path_hd", liveRoom.pullRTMPUrl)
        intent.putExtra("is_living", true)
        intent.putExtra("cover_image_url", liveRoom.roomCoverImageUrl)
        intent.putExtra("title", liveRoom.roomName)
        startActivity(intent)
    } else {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("path", liveRoom.pullRTMPUrl)
        intent.putExtra("path_hd", liveRoom.pullRTMPUrl)
        intent.putExtra("is_living", true)
        intent.putExtra("cover_image_url", liveRoom.roomCoverImageUrl)
        intent.putExtra("title", liveRoom.roomName)
        startActivity(intent)
    }
}