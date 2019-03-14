package com.jackchance.live360.Api

import com.jackchance.live360.model.Translation
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import tech.linjiang.pandora.Pandora

/**
 *  Created by lijiachang on 2018/11/26
 */
object DataApi {

    val okHttpClient = OkHttpClient.Builder().addInterceptor(Pandora.get().interceptor).build()

    private val dataService: DataService = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://fy.iciba.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DataService::class.java)

    private interface DataService {
        @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
        fun getData(): Call<Translation>
    }

    fun getDataCall(): Call<Translation> {
        return dataService.getData()
    }

}