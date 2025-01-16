package com.example.kidseducation.client

import com.example.kidseducation.response.account.LoginResponse
import com.example.kidseducation.response.account.RegisterRequest
import com.example.kidseducation.response.account.RegisterResponse
import com.example.kidseducation.response.collection.CollectionCategoryResponse
import com.example.kidseducation.response.collection.SaveResponse
import com.example.kidseducation.response.collection.UserCollectionResponse
import com.example.kidseducation.response.data.QuizQuestionResponse
import com.example.kidseducation.response.data.UserAnswerResponse
import com.example.kidseducation.response.quizcategory.QuizCategoryResponse
import com.example.kidseducation.response.quizcategory.QuizProgressResponse
import com.example.kidseducation.response.user.AnswerRequest
import com.example.kidseducation.response.user.UserQuizResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @POST("SaveObject")
    @FormUrlEncoded
    fun saveObject(
        @Field("id_user") idUser: String,
        @Field("object_name") objectName: String,
        @Field("object_description") objectDescription: String,
        @Field("object_image_url") imageUrl: String,
        @Field("id_kategori") categoryId: Int
    ): Call<SaveResponse>

    @GET("UserCollection")
    fun getUserCollection(
        @Query("id_user") idUser: String,
        @Query("id_kategori") idKategoriObjek: String
    ): Call<ArrayList<UserCollectionResponse>>

    @GET("UserAnswer")
    fun getUserAnswer(
        @Query("id_user") idUser: String,
        @Query("id_kategori") idKategori: String
    ): Call<List<UserAnswerResponse>>

    @POST("submitAnswer")
    fun postAnswer(@Body answerRequest: AnswerRequest): Call<Void>

    @GET("QuizQuestion")
    fun getQuizQuestion(
        @Query("id_kategori") idKategori: String,
        @Query("id_pertanyaan") idQuiz: Int
    ): Call<ArrayList<QuizQuestionResponse>>

    @POST("RegisterAccount")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET("UserQuiz")
    fun getUserQuiz(
        @Query("id_kategori") idKategori: String,
        @Query("id_user") idUser: String
    ): Call<ArrayList<UserQuizResponse>>

    @GET("CollectionCategory")
    fun getCollectionCategory(): Call<ArrayList<CollectionCategoryResponse>>

    @GET("quizprogress")
    fun getQuizProgress(@Query("id_user") idUser: String): Call<ArrayList<QuizProgressResponse>>

    @GET("quizcategory")
    fun getQuizCategory(): Call<ArrayList<QuizCategoryResponse>>

    @FormUrlEncoded
    @POST("account")
    fun postLogin(
        @Field("username") username:String,
        @Field("password") password:String,
    ): Call<LoginResponse>
}