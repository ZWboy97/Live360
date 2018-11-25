package com.jackchance.live360.videolist.data

import com.jackchance.live360.model.User

/**
 *  Created by lijiachang on 2018/11/25
 */
class Publisher(
        id: Int = 0,
        nickName: String = "用户",
        avtarUrl: String = ""
) : User(id, nickName, avtarUrl) {

}