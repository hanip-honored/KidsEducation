package com.example.kidseducation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.quizcategory.QuizCategoryResponse
import com.example.kidseducation.response.user.UserQuizResponse
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgressActivity : AppCompatActivity() {
    private var idKategori: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progress)

        var idKategori = intent.getStringExtra("id_kategori") ?: "Unknown"
        val idUser = intent.getStringExtra("id_user") ?: "Unknown"

        val imgHeader: ImageView = findViewById(R.id.imgHeader)
        val img1: ImageView = findViewById(R.id.img1)
        val img2: ImageView = findViewById(R.id.img2)
        val img3: ImageView = findViewById(R.id.img3)
        val back: ImageView = findViewById(R.id.back)

        back.setOnClickListener {
            val intentKuis = Intent(this, HomeActivity::class.java).apply {
                putExtra("ID_USER", idUser)
                putExtra("USERNAME", intent.getStringExtra("USERNAME"))
            }
            startActivity(intentKuis)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        RetrofitClient.instance.getUserQuiz(idKategori, idUser).enqueue(
            object : Callback<ArrayList<UserQuizResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<UserQuizResponse>>,
                    response: Response<ArrayList<UserQuizResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val userQuizList = response.body()

                        userQuizList?.let { quizList ->
                            if (quizList.isNotEmpty()) {
                                val title: TextView = findViewById(R.id.txtProgres)
                                title.text = quizList[0].nama_pelajaran

                                val imageUrlHeader = "${RetrofitClient.URL_QUIZ}${quizList[0].nama_pelajaran}/header.png"
                                val image1 = "${RetrofitClient.URL_QUIZ}${quizList[0].nama_pelajaran}/img1.png"
                                val image2 = "${RetrofitClient.URL_QUIZ}${quizList[0].nama_pelajaran}/img2.png"
                                val image3 = "${RetrofitClient.URL_QUIZ}${quizList[0].nama_pelajaran}/img3.png"

                                Picasso.get().load(imageUrlHeader).into(imgHeader)
                                Picasso.get().load(image1).into(img1)
                                Picasso.get().load(image2).into(img2)
                                Picasso.get().load(image3).into(img3)

                                idKategori = quizList[0].id_kategori
                            } else {
                                Log.e("ProgressActivity", "Data tidak tersedia.")
                            }
                        }
                    } else {
                        Log.e("ProgressActivity", "Response tidak berhasil. Code: ${response.code()}, Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserQuizResponse>>, t: Throwable) {
                    Log.e("ProgressActivity", "Error: ${t.message}")
                }
            }
        )

        for (level in 1..10) {
            val levelId = resources.getIdentifier("txtLevel$level", "id", packageName)
            val txtLevel = findViewById<TextView>(levelId)

            txtLevel.setOnClickListener {
                val intent = Intent(this, KuisActivity::class.java)
                intent.putExtra("level", level)
                intent.putExtra("id_kategori", idKategori)
                startActivity(intent)
            }
        }
    }
}