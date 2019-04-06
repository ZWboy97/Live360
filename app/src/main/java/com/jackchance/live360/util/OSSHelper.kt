package com.jackchance.live360.util

import android.content.Context
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult

class OSSHelper {

    var ossClient: OSSClient? = null
    var isReady = true

    private fun initOSSClient(applicationContext: Context?) {
        if (applicationContext == null) {
            return
        }
        val conf = ClientConfiguration()
        conf.apply {
            connectionTimeout = 15000
            socketTimeout = 15000
            maxConcurrentRequest = 5
            maxErrorRetry = 2
            OSSLog.enableLog()
        }
        val credentialProvider = OSSAuthCredentialsProvider("http://39.106.194.43:7080")
        ossClient = OSSClient(applicationContext, "http://oss-cn-beijing.aliyuncs.com", credentialProvider, conf)
        isReady = true
    }

    fun asyncPutObjectToLiveBucket(objectKey: String, path: String, callback: OSSCallback): OSSAsyncTask<PutObjectResult>? {
        if (!isReady) {
            initOSSClient(applicationContext)
        }
        val putObjectRequest = PutObjectRequest(BUCKET_NAME_LIVE_360, objectKey, path)
        putObjectRequest.setProgressCallback { request, currentSize, totalSize ->
            callback.onProgress(request, currentSize, totalSize)
        }
        val task = ossClient?.asyncPutObject(putObjectRequest, object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                callback.onSuccess(request, result)
            }

            override fun onFailure(request: PutObjectRequest?, clientException: ClientException?, serviceException: ServiceException?) {
                callback.onFailure(request, clientException, serviceException)
            }
        })
        return task
    }

    interface OSSCallback {

        fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?)

        fun onFailure(request: PutObjectRequest?, clientException: ClientException?,
                      serviceException: ServiceException?)

        fun onProgress(request: PutObjectRequest, currentSize: Long, totalSize: Long)

    }


    companion object {

        private var applicationContext: Context? = null

        const val BUCKET_NAME_LIVE_360 = "live360"

        fun initOSS(applicationContext: Context) {
            this.applicationContext = applicationContext
            INSTANCE
        }

        fun getImageUrlFromKey(objectKey: String): String {
            return "https://live360.oss-cn-beijing.aliyuncs.com/${objectKey}"
        }

        val INSTANCE: OSSHelper by lazy {
            OSSHelper().apply {
                initOSSClient(applicationContext)
            }
        }
    }
}