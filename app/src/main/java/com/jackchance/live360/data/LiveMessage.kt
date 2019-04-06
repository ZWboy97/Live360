package com.jackchance.live360.data

import com.jackchance.live360.videolist.data.Publisher

/**
 *  Created by lijiachang on 2019/3/14
 */
class LiveMessage : BaseData() {
    var id: Int = 0
    var liveRoomId: Int = 0
    var publisher: Publisher? = null
    var name: String = ""
    var desc: String = ""
    var publishTime: Long = 0L
    var message: String = ""
    var coverImageUrl: String = ""
    var liveStatus: Int = 0
    var isVr: Boolean = false
}