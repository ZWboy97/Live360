package com.jackchance.live360.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.jackchance.live360.R
import com.jackchance.live360.data.LiveRoom
import com.jackchance.live360.util.*
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.content.Intent


class LiveDetailActivity : AppCompatActivity() {
    private val coverImageView: ImageView by viewById(R.id.live_cover_image)
    private val liveTitleTextView: TextView by viewById(R.id.live_title_text)
    private val liveDescTextView: TextView by viewById(R.id.live_desc_text)
    private val liveModelTextView: TextView by viewById(R.id.live_model_text)
    private val liveStartTime: TextView by viewById(R.id.start_time_text)
    private val liveEndTime: TextView by viewById(R.id.end_time_text)
    private val livePushUrlText: TextView by viewById(R.id.push_url_text)
    private val livePullUrlText: TextView by viewById(R.id.pull_url_text)
    private val startLiveButton: Button by viewById(R.id.start_live_button)
    private val livePullHLSText: TextView by viewById(R.id.pull_url_hls_text)
    private val livePlaybackUrlText: TextView by viewById(R.id.playback_url_text)

    private var liveRoom: LiveRoom? = null
    private var isUserModel: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_detail)
        val json = intent.getStringExtra("liveroom")
        if (json.isNullOrEmpty()) {
            return
        }
        if (intent.hasExtra("user_model")) {
            isUserModel = intent.getBooleanExtra("user_model", true)
        }
        liveRoom = JsonUtil.jsonToObject(json, LiveRoom::class.java)
        startLiveButton.text = if (isUserModel) "观看直播" else "开始直播"
        coverImageView.loadUrl(liveRoom?.roomCoverImageUrl ?: "")
        liveTitleTextView.text = liveRoom?.roomName
        liveDescTextView.text = liveRoom?.roomDesc
        liveModelTextView.text = if (liveRoom?.isVR ?: false) "全景直播模式" else "普通直播模式"
        liveStartTime.text = liveRoom?.startTime?.toMMddHHmm()
        liveEndTime.text = liveRoom?.endTime?.toMMddHHmm()
        livePushUrlText.text = liveRoom?.pushUrl
        livePullUrlText.text = liveRoom?.pullRTMPUrl
        livePullHLSText.text = liveRoom?.pullHLSUrl
        livePlaybackUrlText.text = liveRoom?.playbackUrl
        startLiveButton.setOnClickListener {
            if (isUserModel) {
                liveRoom?.let {
                    this.toLiveActivity(it)
                }
            } else {
                //启动 instat360 app
                if (checkPackInfo("com.arashivision.insta360one")) {
                    val intent = packageManager.getLaunchIntentForPackage("com.arashivision.insta360one")
                    if (intent != null) {
                        intent.putExtra("type", "110")
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "检测到您未安装全景直播应用", Toast.LENGTH_SHORT).show()
                }
            }
        }
        livePushUrlText.setOnClickListener {
            copyToClipboard(livePushUrlText.text.toString())
        }
        livePullUrlText.setOnClickListener {
            copyToClipboard(livePullUrlText.text.toString())
        }
        livePullHLSText.setOnClickListener {
            copyToClipboard(livePullUrlText.text.toString())
        }
        livePlaybackUrlText.setOnClickListener {
            copyToClipboard(livePlaybackUrlText.text.toString())
        }
    }

    private fun copyToClipboard(text: String?) {
        if (text.isNullOrEmpty()) {
            return
        }
        val systemService: ClipboardManager = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        systemService.primaryClip = ClipData.newPlainText("text", text)
        Toast.makeText(this, "复制到剪贴板", Toast.LENGTH_SHORT).show()
    }

    private fun checkPackInfo(packname: String): Boolean {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packname, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packageInfo != null
    }
}
