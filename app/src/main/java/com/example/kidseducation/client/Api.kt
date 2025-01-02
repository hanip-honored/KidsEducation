package com.example.kidseducation.client

import com.example.kidseducation.response.account.LoginResponse
import com.example.kidseducation.response.quizcategory.QuizCategoryResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("quizcategory")
    fun getQuizCategory(): Call<ArrayList<QuizCategoryResponse>>

    @FormUrlEncoded
    @POST("account")
    fun postLogin(
        @Field("username") username:String,
        @Field("password") password:String,
    ): Call<LoginResponse>
}