package com.jackchance.live360.Api

import com.jackchance.live360.data.LiveMessage
import com.jackchance.live360.data.LiveRoom
import com.jackchance.live360.model.Translation
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tech.linjiang.pandora.Pandora

/**
 *  Created by lijiachang on 2018/11/26
 */
object DataApi {

    val okHttpClient = OkHttpClient.Builder().addInterceptor(Pandora.get().interceptor).build()

    private val dataService: DataService = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://39.106.194.43:8080/live360")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DataService::class.java)

    private interface DataService {

        @POST("/livemessages")
        fun postLiveMessage(@Body liveMessage: LiveMessage): Call<LiveMessage>

        @GET("/livemessages")
        fun getLiveMessages(): Call<LiveMessage>

        @POST("/liverooms")
        fun postLiveRooms(@Body liveRoom: LiveRoom): Call<LiveRoom>

        @GET("liverooms")
        fun getLiveRooms(): Call<LiveRoom>

    }

    fun postLiveMessage(liveMessage: LiveMessage): Call<LiveMessage> {
        return dataService.postLiveMessage(liveMessage)
    }

    fun getLiveMessages(): Call<LiveMessage> {
        return dataService.getLiveMessages()
    }

    fun postLiveRooms(liveRoom: LiveRoom): Call<LiveRoom> {
        return dataService.postLiveRooms(liveRoom)
    }

    fun getLiveRooms(): Call<LiveRoom> {
        return dataService.getLiveRooms()
    }


}