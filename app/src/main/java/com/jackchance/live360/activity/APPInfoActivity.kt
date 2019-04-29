package com.jackchance.live360.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.jackchance.live360.R
import com.jackchance.live360.util.viewById
import com.jackchance.live360.util.visible

class APPInfoActivity : AppCompatActivity() {

    private val checkVersionButton: Button by viewById(R.id.check_version_button)
    private val messageText: TextView by viewById(R.id.message_text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appinfo)
        checkVersionButton.setOnClickListener {
            checkVersionButton.visible = false
            messageText.visible = true
        }
    }
}
