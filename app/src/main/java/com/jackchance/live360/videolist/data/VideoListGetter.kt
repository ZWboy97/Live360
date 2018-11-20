package com.jackchance.live360.videolist.data

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by lijiachang on 2018/11/20
 */
object VideoListBuilder {

    private val videoList: MutableList<Video> = ArrayList()

    fun build() {

    }

    fun getVideoList(): MutableList<Video> {
        return videoList
    }
}
