package com.example.kidseducation

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.data.UserAnswerResponse
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KuisReviewActivity : AppCompatActivity() {
    private lateinit var level: TextView
    private lateinit var progress: ProgressBar
    private lateinit var soal: TextView
    private lateinit var image: ImageView
    private lateinit var jawabanUser: TextView
    private lateinit var validasi: TextView
    private lateinit var ulasan: TextView
    private lateinit var prev: Button
    private lateinit var next: Button

    private var userAnswers: List<UserAnswerResponse> = listOf()
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kuis_review)

        level = findViewById(R.id.levelReview)
        progress = findViewById(R.id.progressBarReview)
        soal = findViewById(R.id.soalReview)
        image = findViewById(R.id.imageReview)
        jawabanUser = findViewById(R.id.jawabanUser)
        validasi = findViewById(R.id.validasi)
        ulasan = findViewById(R.id.ulasan)
        prev = findViewById(R.id.buttonSebelumnya)
        next = findViewById(R.id.buttonSelanjutnya)

        val idUser = intent.getStringExtra("id_user") ?: ""
        val idKategori = intent.getStringExtra("id_kategori") ?: ""

        loadUserAnswers(idUser, idKategori)

        prev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                displayAnswer(currentIndex)
            } else {
                Toast.makeText(this, "Sudah di pertanyaan pertama!", Toast.LENGTH_SHORT).show()
            }
        }

        next.setOnClickListener {
            if (currentIndex < 10) {
                currentIndex++
                displayAnswer(currentIndex)
            } else {
                val intent = Intent(this, ProgressActivity::class.java)
                intent.putExtra("id_user", intent.getStringExtra("id_user"))
                intent.putExtra("id_kategori", intent.getStringExtra("id_kategori"))
                startActivity(intent)
                finish()
            }
        }
    }

    // Fungsi untuk memuat data jawaban user dari server
    private fun loadUserAnswers(idUser: String, idKategori: String) {
        RetrofitClient.instance.getUserAnswer(idUser, idKategori).enqueue(object :
            Callback<List<UserAnswerResponse>> { // Menggunakan List
            override fun onResponse(
                call: Call<List<UserAnswerResponse>>,
                response: Response<List<UserAnswerResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    userAnswers = response.body()!!
                    if (userAnswers.isNotEmpty()) {
                        displayAnswer(currentIndex)
                    } else {
                        Toast.makeText(this@KuisReviewActivity, "Tidak ada data jawaban!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@KuisReviewActivity, "Gagal memuat data jawaban!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserAnswerResponse>>, t: Throwable) {
                Toast.makeText(this@KuisReviewActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayAnswer(index: Int) {
        val answer = userAnswers[index]

        level.text = "${index + 1}/10"
        progress.progress = (index + 1) * 10
        soal.text = answer.soal
        jawabanUser.text = answer.jawaban

        val drawable = jawabanUser.background as GradientDrawable

        if (answer.is_correct == 1) {
            validasi.text = "Benar!"
            validasi.setTextColor(resources.getColor(R.color.correct_green, null)) // #A2DDC2
            drawable.setStroke(3, resources.getColor(R.color.correct_green, null)) // Ubah stroke ke hijau
        } else {
            validasi.text = "Salah!"
            validasi.setTextColor(resources.getColor(R.color.incorrect_red, null)) // #FF1E00
            drawable.setStroke(3, resources.getColor(R.color.incorrect_red, null)) // Ubah stroke ke merah
        }

        ulasan.text = answer.ulasan

        val imageSoal = "${RetrofitClient.URL_QUIZ_SOAL}${answer.id_quiz}/${answer.gambar_ulasan}"
        Picasso.get().load(imageSoal).into(image)
    }

}