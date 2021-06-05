package com.eyeoh.androidquizdekd.Service

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/v1/banner")
    abstract fun getBanner(): retrofit2.Call<ResponseBody>

    @GET("api/v1/list")
    abstract fun getItemList(
        @Query("page") page: Int,
        @Query("limit") limit : Int = 20): retrofit2.Call<ResponseBody>

    @GET("api/v1/list")
    abstract fun getsortedList(
        @Query("sortBy") sortBy: String,
        @Query("order") order: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): retrofit2.Call<ResponseBody>

    @GET("api/v1/list/{id}")
    abstract fun getItemListInfo(@Path("id") id: Int): retrofit2.Call<ResponseBody>
}