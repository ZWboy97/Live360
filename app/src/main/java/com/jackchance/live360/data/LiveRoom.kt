package com.jackchance.live360.data

/**
 *  Created by lijiachang on 2019/3/14
 */
class LiveRoom(
    var id: Int = 0,
    var hostId: Int = 0,    //主播，发布者id
    var roomName: String = "",
    var roomDesc: String = "",
    var roomCoverImageUrl: String = "",
    var roomStatus: LiveStatus = LiveStatus.WAITING,
    var pullRTMPUrl: String = "",
    var pullHLSUrl: String = "",
    var playbackUrl: String = "",
    var startTime: Long = 0L,
    var endTime: Long = 0L,
    var isVR: Boolean = false
) : BaseData() {

    companion object {
        enum class LiveStatus {
            WAITING,
            LIVING,
            PREPARING,
            READY,
            ERROR
        }
    }

}
