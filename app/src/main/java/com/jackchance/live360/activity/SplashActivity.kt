package com.jackchance.live360.activity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.WindowManager
import android.widget.TextView
import com.jackchance.live360.R
import com.jackchance.live360.util.toHomeActivityt
import com.jackchance.live360.util.toMainActivity
import com.jackchance.live360.util.viewById

/**
 * Created by lijiachang on 2018/11/20
 */

class SplashActivity : BaseActivity() {

    private val appNameText: TextView by viewById(R.id.app_name)

    private val handler = Handler(HandlerCallBack())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        handler.sendEmptyMessageDelayed(MSG_TO_MAINACTIVITY, WAIT_DELAY)

    }

    private inner class HandlerCallBack : Handler.Callback {

        override fun handleMessage(msg: Message?): Boolean {

            when (msg?.what) {
                MSG_TO_MAINACTIVITY -> {
                    toHomeActivityt()
                    finish()
                }
            }
            return true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler.removeMessages(MSG_TO_MAINACTIVITY)
    }

    companion object {
        const val MSG_TO_MAINACTIVITY = 0

        const val WAIT_DELAY: Long = 1500L
    }

}
