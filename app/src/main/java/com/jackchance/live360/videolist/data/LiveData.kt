package com.jackchance.live360.videolist.data

/**
 *  Created by lijiachang on 2018/11/24
 */
data class LiveData(
        var id: Int = 0,
        var name: String = "全景直播",
        var description: String = "",
        var imageUrl: String = "",
        var hlsUrl: String = "",
        var rtmpUrl: String = "",
        var startTime: Long = 0,
        var isFinish: Boolean = true,
        var publisher: Publisher? = null,
        var publisherMessage: String = "",
        var isVr: Boolean = false
)