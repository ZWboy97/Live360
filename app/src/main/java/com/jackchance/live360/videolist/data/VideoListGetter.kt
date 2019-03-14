package com.jackchance.live360.videolist.data

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by lijiachang on 2018/11/20
 */
object VideoListBuilder {

    private val videoList: MutableList<LiveData> = ArrayList()

    fun build() {

    }

    fun clearVideoList(){
        videoList.clear()
    }

    fun getVideoList(onSuccess: ()->Unit = {}): MutableList<LiveData> {
        videoList.clear()
        videoList.add(LiveData().apply {
            name = "RTMP全景视频直播"
            description = "哈哈，我的第一个全景直播个直播"
            imageUrl = "http://live360bucket.oss-cn-beijing.aliyuncs.com/image/background.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            publisherMessage = "我的第一个全景视频直播，快来观看吧！！！ 😄😄😄"
            rtmpUrl = "rtmp://39.106.194.43/live/livestream.flv"
            isVr = true
        })
        videoList.add(LiveData().apply {
            name = "非全景直播"
            description = "哈哈，我的第er个直播"
            imageUrl = "http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            publisherMessage = "哈哈=---圣诞节发就是老地方👌就圣诞节发 i阿斯顿放假了阿斯顿家乐福 i "
            rtmpUrl = "rtmp://39.106.194.43/live/livestream.flv"
            isVr = false
        })
        videoList.add(LiveData().apply {
            name = "全景视频点播播放"
            description = "哈哈，我的第一个直播"
            imageUrl = "http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg"

            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            publisherMessage = "大美山西之---壶口瀑布👏👏👏 "
            rtmpUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/vrresource/%E5%A4%A7%E7%BE%8E%E5%B1%B1%E8%A5%BF-%E5%A3%B6%E5%8F%A3%E7%80%91%E5%B8%83-VR%E8%A7%86%E9%A2%91%E7%89%87%E6%BA%90-UtoVR.mp4"
            isVr = true
        })
        onSuccess.invoke()

        return videoList
    }
}
