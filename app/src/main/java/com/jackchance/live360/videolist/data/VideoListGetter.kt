package com.jackchance.live360.videolist.data

import com.jackchance.live360.Api.DataApi
import com.jackchance.live360.data.LiveRoom
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by lijiachang on 2018/11/20
 */
object VideoListBuilder {

    private val videoList: MutableList<LiveRoom> = ArrayList()

    fun build() {

    }

    fun clearVideoList() {
        videoList.clear()
    }

    fun getVideoList(onSuccess: () -> Unit = {}): MutableList<LiveRoom> {
        videoList.clear()
        DataApi.getLiveRooms().enqueue(object : Callback<List<LiveRoom>> {
            override fun onFailure(call: Call<List<LiveRoom>>, t: Throwable) {

            }

            override fun onResponse(call: Call<List<LiveRoom>>, response: Response<List<LiveRoom>>) {
                response.body()?.let {
                    videoList.addAll(it)
                }
                onSuccess.invoke()
            }
        })

        return videoList
    }
}
