package com.jackchance.live360.Const

class OSSConfig {



    companion object {
        const val OSS_ENDPOITN: String = "http://oss-cn-shanghai.aliyuncs.com"
        const val OSS_CALLBACK_URL:String = ""

        const val BUCKET_NAME = ""
        const val OSS_ACCESS_KEY_ID = ""
        const val OSS_ACCESS_KEY_SECRET = ""

        val DOWNLOAD_SUC = 1
        val DOWNLOAD_Fail = 2
        val UPLOAD_SUC = 3
        val UPLOAD_Fail = 4
        val UPLOAD_PROGRESS = 5
        val LIST_SUC = 6
        val HEAD_SUC = 7
        val RESUMABLE_SUC = 8
        val SIGN_SUC = 9
        val BUCKET_SUC = 10
        val GET_STS_SUC = 11
        val MULTIPART_SUC = 12
        val STS_TOKEN_SUC = 13
        val FAIL = 9999
        val REQUESTCODE_AUTH = 10111
        val REQUESTCODE_LOCALPHOTOS = 10112
    }

}