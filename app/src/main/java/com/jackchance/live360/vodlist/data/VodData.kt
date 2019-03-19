package com.jackchance.live360.vodlist.data

import com.jackchance.live360.livelist.data.Publisher

/**
 *  Created by lijiachang on 2018/12/1
 */
data class VodData(
        var id: Int = 0,
        var name: String = "全景视频",
        var description: String = "欢迎点击观看",
        var imageUrl: String = "",
        var resourceUrl: String = "",
        var kindTag: String = "其他",
        var clickTimes: Int = 0,
        var createTime: Long = 0L,
        var publisher: Publisher? = null,
        var isVr: Boolean = false
)