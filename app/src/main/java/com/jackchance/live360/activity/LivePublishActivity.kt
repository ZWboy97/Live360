package com.jackchance.live360.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.FragmentActivity
import android.widget.*
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.jackchance.live360.Api.DataApi
import com.jackchance.live360.R
import com.jackchance.live360.data.LiveRoom
import com.jackchance.live360.util.MyImageEngine
import com.jackchance.live360.util.OSSHelper
import com.jackchance.live360.util.visible
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.data.Type
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class LivePublishActivity : FragmentActivity() {

    private var currentTime: Long
        get() = Date().time
        set(value) {
            value
        }

    private var selectImageUri: Uri? = null
    private var ossTask: OSSAsyncTask<PutObjectResult>? = null
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var isVRModel: Boolean = false
    private var isPostLiveSuccess: Boolean = false
    private var isUploadImageSuccess: Boolean = false
    private var coverImageKey: String? = null
    private var descText: String? = null
    private var titleText: String? = null
    private var liveRoom: LiveRoom? = null

    private lateinit var liveCoverImageView: ImageView
    private lateinit var imageUploadLabel: TextView
    private lateinit var liveTitleEditTextView: TextView
    private lateinit var liveDescEditText: EditText
    private lateinit var liveModelSwitch: Switch
    private lateinit var liveStartTime: TextView
    private lateinit var liveEndTime: TextView
    private lateinit var livePublishButton: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_live_publish)
        liveCoverImageView = findViewById(R.id.live_cover_image)
        imageUploadLabel = findViewById(R.id.image_upload_lable)
        liveTitleEditTextView = findViewById(R.id.live_title_text)
        liveDescEditText = findViewById(R.id.live_desc_text)
        liveModelSwitch = findViewById(R.id.live_model_text)
        liveStartTime = findViewById(R.id.start_time_picker)
        liveEndTime = findViewById(R.id.end_time_picker)
        livePublishButton = findViewById(R.id.submit_button)
        progressBar = findViewById(R.id.progressBar)
        liveCoverImageView.setOnClickListener {
            selectLiveCoverImage()
        }
        liveModelSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isVRModel = true
                Toast.makeText(this, "开启全景直播模式", Toast.LENGTH_SHORT).show()
            } else {
                isVRModel = false
            }
        }
        liveStartTime.setOnClickListener {
            val startTimePickerDialog = TimePickerDialog.Builder()
                    .setType(Type.MONTH_DAY_HOUR_MIN)
                    .setTitleStringId("直播开始时间")
                    .setCallBack { timePickerView, millseconds ->
                        if (currentTime > millseconds) {
                            Toast.makeText(this, "开始时间应大于当前时间", Toast.LENGTH_SHORT).show()
                            startTime = currentTime
                        } else {
                            startTime = millseconds
                        }
                        val date = Date(millseconds)
                        val startTimeString = SimpleDateFormat("MM-dd HH:mm").format(date)
                        liveStartTime.text = startTimeString
                    }
                    .build()
            startTimePickerDialog.show(supportFragmentManager, "月_日_时_分")
        }
        liveEndTime.setOnClickListener {
            val endTimePickerDialog = TimePickerDialog.Builder()
                    .setType(Type.MONTH_DAY_HOUR_MIN)
                    .setTitleStringId("直播结束时间")
                    .setCallBack { timePickerView, millseconds ->
                        if (startTime > millseconds) {
                            Toast.makeText(this, "结束时间应该大于开始时间", Toast.LENGTH_SHORT).show()
                            endTime = startTime
                        } else {
                            endTime = millseconds
                        }
                        val date = Date(millseconds)
                        val endTimeString = SimpleDateFormat("MM-dd HH:mm").format(date)
                        liveEndTime.text = endTimeString
                    }
                    .build()
            endTimePickerDialog.show(supportFragmentManager, "月_日_时_分")
        }
        livePublishButton.setOnClickListener {
            if (checkInput()) {
                livePublishButton.isEnabled = false
                isPostLiveSuccess = false
                isUploadImageSuccess = false
                progressBar.visible = true
                uploadCoverImage()
                postLiveRoomAndMessage()
            }
        }
    }

    private fun postLiveRoomAndMessage() {
        if (isPostLiveSuccess) {
            return
        }
        val liveRoom = LiveRoom().apply {
            this.hostId = 1
            this.endTime = this@LivePublishActivity.endTime
            this.isVR = isVRModel
            this.roomCoverImageUrl = OSSHelper.getImageUrlFromKey(coverImageKey ?: "")
            this.roomDesc = descText ?: ""
            this.roomName = titleText ?: ""
            this.startTime = this@LivePublishActivity.startTime
        }

        DataApi.postLiveRooms(liveRoom).enqueue(object : Callback<LiveRoom> {
            override fun onFailure(call: Call<LiveRoom>, t: Throwable) {
                Toast.makeText(this@LivePublishActivity, "直播间创建失败，请稍后重试！", Toast.LENGTH_SHORT).show()
                uploadAndPostFinish()
            }

            override fun onResponse(call: Call<LiveRoom>, response: Response<LiveRoom>) {
                this@LivePublishActivity.liveRoom = response.body()
                isPostLiveSuccess = true
                if (isUploadImageSuccess) {
                    uploadAndPostFinish()
                    toSuccessActivity()
                }
            }
        })
    }

    private fun checkInput(): Boolean {
        titleText = liveTitleEditTextView.text.toString()
        descText = liveDescEditText.text.toString()
        if (selectImageUri == null) {
            Toast.makeText(this, "未选择封面图片", Toast.LENGTH_SHORT).show()
            return false
        } else if (titleText.isNullOrEmpty()) {
            Toast.makeText(this, "未填写直播标题！", Toast.LENGTH_SHORT).show()
            return false
        } else if (descText.isNullOrEmpty()) {
            Toast.makeText(this, "未填写直播简介！", Toast.LENGTH_SHORT).show()
            return false
        } else if (startTime < currentTime) {
            Toast.makeText(this, "开始时间小于当前时间！", Toast.LENGTH_SHORT).show()
            return false
        } else if (startTime > endTime) {
            Toast.makeText(this, "结束时间小于开始时间！", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    @AfterPermissionGranted(1)
    private fun selectLiveCoverImage() {
        val hasPermission = EasyPermissions.hasPermissions(this
                , Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (hasPermission) {
            startImageSelect()
        } else {
            EasyPermissions.requestPermissions(this,
                    "upload live cover image", 1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun startImageSelect() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(false)
                .maxSelectable(1)
                .thumbnailScale(0.85f)
                .imageEngine(MyImageEngine())
                .forResult(REQUEST_CODE_CHOOSE)
    }

    private fun uploadCoverImage() {
        if (isUploadImageSuccess) {
            return
        }
        var filePath = ""
        var contentResolver = this.contentResolver
        val cursor = contentResolver.query(selectImageUri,
                arrayOf(MediaStore.Images.Media.DATA), null, null, null)
        if (cursor.moveToFirst()) {
            try {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = cursor.getString(columnIndex)
            } catch (e: IllegalArgumentException) {
                cursor.close()
                uploadAndPostFinish()
                return
            }
        }
        cursor.close()
        if (coverImageKey == null) {
            coverImageKey = "live360_${Date().time}"
        }
        val objectKey = coverImageKey ?: ""
        ossTask = OSSHelper.INSTANCE.asyncPutObjectToLiveBucket(objectKey
                , File(filePath).absolutePath, object : OSSHelper.OSSCallback {

            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                isUploadImageSuccess = true
                coverImageKey = null
                livePublishButton.post {
                    if (isPostLiveSuccess) {
                        uploadAndPostFinish()
                        toSuccessActivity()
                    }
                }
            }

            override fun onFailure(request: PutObjectRequest?, clientException: ClientException?, serviceException: ServiceException?) {
                Toast.makeText(this@LivePublishActivity, "图片上传失败，请稍后重试！", Toast.LENGTH_LONG).show()
                livePublishButton.post {
                    uploadAndPostFinish()
                }
            }

            override fun onProgress(request: PutObjectRequest, currentSize: Long, totalSize: Long) {
            }
        })
    }

    private fun uploadAndPostFinish() {
        livePublishButton.isEnabled = true
        progressBar.visible = false
        isPostLiveSuccess = false
        isUploadImageSuccess = false
        isPostLiveSuccess = false
    }

    private fun toSuccessActivity() {
        val intent = Intent(this@LivePublishActivity, LiveDetailActivity::class.java)
        intent.putExtra("liveroom", liveRoom?.writeJson())
        intent.putExtra("user_model", false)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        ossTask?.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            val uriList = Matisse.obtainResult(data)
            if (uriList.isNotEmpty()) {
                selectImageUri = uriList.get(0)
                liveCoverImageView.setImageURI(selectImageUri)
                imageUploadLabel.visible = false
                isUploadImageSuccess = false
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {

        private const val REQUEST_CODE_CHOOSE = 0
    }
}
