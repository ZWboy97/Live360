package com.jackchance.live360

import android.app.Application
import com.jackchance.live360.util.OSSHelper

class Live360Application : Application() {

    override fun onCreate() {
        super.onCreate()
        OSSHelper.initOSS(applicationContext)
    }
}