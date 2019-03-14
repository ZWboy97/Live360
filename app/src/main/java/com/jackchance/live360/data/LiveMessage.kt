package com.jackchance.live360.data

import com.jackchance.live360.videolist.data.Publisher

/**
 *  Created by lijiachang on 2019/3/14
 */
class LiveMessage(
    var id: Int = 0,
    var liveRoomId: Int = 0,
    var publisher: Publisher? = null,
    var name: String = "",
    var desc: String = "",
    var publishTime: Long = 0L,
    var message: String = "",
    var coverImageUrl: String,
    var liveStatus: LiveRoom.Companion.LiveStatus = LiveRoom.Companion.LiveStatus.WAITING,
    var isVr: Boolean = false
) : BaseData()