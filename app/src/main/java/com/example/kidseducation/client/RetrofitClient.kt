package com.example.kidseducation.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "http://10.200.48.152:80/edukids_api/index.php/"
    val URL = "http://10.200.48.152/edukids_api/gambar_kategori/"
    val URL_COLLECTION = "http://10.200.48.152/edukids_api/gambar_collection/"
    val URL_QUIZ = "http://10.200.48.152/edukids_api/quiz/"
    val URL_QUIZ_SOAL = "http://10.200.48.152/edukids_api/soal/"

    val instance:Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java)
    }
}