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
            name = "壶口瀑布"
            description = "大美山西系列，壶口瀑布"
            imageUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            resourceUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/vrresource/%E5%A4%A7%E7%BE%8E%E5%B1%B1%E8%A5%BF-%E5%A3%B6%E5%8F%A3%E7%80%91%E5%B8%83-VR%E8%A7%86%E9%A2%91%E7%89%87%E6%BA%90-UtoVR.mp4"
            isVr = true
        })
        videoList.add(VodData().apply {
            name = "裂谷彩虹"
            description = "裂谷彩虹航拍VR"
            imageUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/image/%E8%A3%82%E8%B0%B7.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            resourceUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/vrresource/output2.mp4"
            isVr = true
        })
        videoList.add(VodData().apply {
            name = "武大樱花季"
            description = "春天最美樱花大学"
            imageUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/image/%E6%AD%A6%E5%A4%A7.jpeg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            resourceUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/vrresource/%E6%AD%A6%E5%A4%A7.mp4"
            isVr = true
        })
        videoList.add(VodData().apply {
            name = "篮球冠军杯"
            description = "深圳宝安体育中心"
            imageUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/image/%E7%AF%AE%E7%90%83%E8%B5%9B.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            resourceUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/vrresource/%E7%AF%AE%E7%90%83%E8%B5%9B.mp4"
            isVr = true
        })
        videoList.add(VodData().apply {
            name = "VR过山车"
            description = "VR体验极速过山车"
            imageUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/image/%E8%BF%87%E5%B1%B1%E8%BD%A6.jpg"
            publisher = Publisher(0,"小毛孩","http://live360bucket.oss-cn-beijing.aliyuncs.com/image/nature1.jpg")
            resourceUrl = "https://live360bucket.oss-cn-beijing.aliyuncs.com/vrresource/%E8%BF%87%E5%B1%B1%E8%BD%A6.mp4"
            isVr = true
        })
        onSuccess.invoke()
        return videoList
    }
}
