package com.jackchance.live360.vod.data

import com.jackchance.live360.videolist.data.Publisher
import java.util.ArrayList

/**
 * Created by lijiachang on 2018/11/20
 */
object VodListBuilder {

    private val videoList: MutableList<VodData> = ArrayList()

    fun build() {

    }

    fun clearVideoList(){
        videoList.clear()
    }

    fun getVodList(onSuccess: ()->Unit = {}): MutableList<VodData> {
        videoList.clear()
        videoList.add(VodData().apply {
            name = "大美山西VR视频"
            description = "大美山西系列--壶口瀑布，VR全景拍摄"
            imageUrl = "http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            resourceUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/vrresource/%E5%A4%A7%E7%BE%8E%E5%B1%B1%E8%A5%BF-%E5%A3%B6%E5%8F%A3%E7%80%91%E5%B8%83-VR%E8%A7%86%E9%A2%91%E7%89%87%E6%BA%90-UtoVR.mp4"
            isVr = true
        })
        videoList.add(VodData().apply {
            name = "大美山西VR视频"
            description = "大美山西系列--泽口古镇，VR全景拍摄"
            imageUrl = "http://live360bucket.oss-cn-beijing.aliyuncs.com/image/background.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            resourceUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/vrresource/%E5%A4%A7%E7%BE%8E%E5%B1%B1%E8%A5%BF-%E5%A3%B6%E5%8F%A3%E7%80%91%E5%B8%83-VR%E8%A7%86%E9%A2%91%E7%89%87%E6%BA%90-UtoVR.mp4"
            isVr = true
        })
        videoList.add(VodData().apply {
            name = "全景视频资源"
            description = "全景视频资源"
            imageUrl = "http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            resourceUrl = "rtmp://39.106.194.43/live/livestream.flv"
            isVr = true
        })
        videoList.add(VodData().apply {
            name = "全景视频资源"
            description = "哈哈，全景视频资源"
            imageUrl = "http://live360bucket.oss-cn-beijing.aliyuncs.com/image/background.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            isVr = true
        })
        onSuccess.invoke()

        return videoList
    }
}
