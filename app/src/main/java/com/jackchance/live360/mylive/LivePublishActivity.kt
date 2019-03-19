package com.jackchance.live360.mylive

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.jackchance.live360.R
import com.jackchance.live360.util.MyPicassoEngine
import com.jackchance.live360.util.viewById
import com.squareup.picasso.Picasso
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class LivePublishActivity : AppCompatActivity() {

    val liveCoverImage: ImageView by viewById(R.id.live_cover_image)

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_publish)
        initView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    fun initView() {
        liveCoverImage.setOnClickListener {
            chooseCoverImage()
        }
    }

    @AfterPermissionGranted(1)
    fun chooseCoverImage() {
        val hasPermission = EasyPermissions.hasPermissions(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (hasPermission) {
            Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .thumbnailScale(0.85f)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .imageEngine(MyPicassoEngine())
                .theme(R.style.Matisse_Dracula)
                .forResult(REQUEST_CODE_IMAGE)
        } else {
            EasyPermissions.requestPermissions(
                this, "", 1,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            val selected = Matisse.obtainResult(data)
            if (!selected.isEmpty()) {
                imageUri = selected[0]
                Picasso.get().load(imageUri).into(liveCoverImage)
            }
        }
    }

    companion object {
        const val REQUEST_CODE_IMAGE = 1
    }

}
