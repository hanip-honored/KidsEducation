package com.example.kidseducation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.data.QuizQuestionResponse
import com.example.kidseducation.response.user.AnswerRequest
import com.example.kidseducation.response.user.UserQuizResponse
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KuisActivity : AppCompatActivity() {
    private var idQuiz = 1
    private lateinit var idKategori: String
    private lateinit var level: TextView
    private lateinit var progress: ProgressBar
    private lateinit var soal: TextView
    private lateinit var image: ImageView
    private lateinit var jawaban1Button: MaterialButton
    private lateinit var jawaban2Button: MaterialButton

    private lateinit var jawabanBenar: String
    private lateinit var idPertanyaan: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kuis)

        val back: ImageView = findViewById(R.id.kembali)
        level = findViewById(R.id.level)
        progress = findViewById(R.id.progressBarKuis)
        soal = findViewById(R.id.soal)
        image = findViewById(R.id.imageSoal)
        jawaban1Button = findViewById(R.id.jawaban1)
        jawaban2Button = findViewById(R.id.jawaban2)


        val sharedPreferences = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("ID_USER", "Unknown")
        val username = sharedPreferences.getString("USERNAME", "Unknown")
        val level = sharedPreferences.getInt("LEVEL", 1)

        idKategori = intent.getStringExtra("id_kategori") ?: "Unknown"
        idQuiz = intent.getIntExtra("level", 1)

        back.setOnClickListener {
            val intentKuis = Intent(this, ProgressActivity::class.java).apply {
                putExtra("id_user", idUser)
                putExtra("id_kategori", idKategori)
            }
            startActivity(intentKuis)
            finish()
        }

        loadQuiz()
    }

    private fun loadQuiz() {
        val idKategoriNumber = idKategori.replace("KP", "").toInt()
        val idQuizGlobal = ((idQuiz - 1) + (idKategoriNumber - 1) * 10) + 1
        RetrofitClient.instance.getQuizQuestion(idKategori, idQuizGlobal).enqueue(
            object : Callback<ArrayList<QuizQuestionResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<QuizQuestionResponse>>,
                    response: Response<ArrayList<QuizQuestionResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val quizList = response.body()

                        quizList?.let { list ->
                            if (list.isNotEmpty()) {
                                level.text = "${idQuiz}/10"
                                progress.progress = idQuiz * 10
                                soal.text = list[0].soal

                                val imageSoal = "${RetrofitClient.URL_QUIZ_SOAL}${list[0].id_quiz}/${list[0].image}"
                                Picasso.get().load(imageSoal).into(image)

                                jawabanBenar = list[0].jawaban
                                val jawabanSalah = list[0].jawaban_salah

                                idPertanyaan = list[0].id_pertanyaan

                                val container: LinearLayout = findViewById(R.id.containerJawaban)

                                val randomIndex = (0..1).random()
                                if (randomIndex == 0) {
                                    jawaban1Button.text = jawabanBenar
                                    jawaban2Button.text = jawabanSalah
                                } else {
                                    jawaban1Button.text = jawabanSalah
                                    jawaban2Button.text = jawabanBenar
                                }

                                jawaban1Button.setOnClickListener { handleAnswer(jawaban1Button.text.toString()) }
                                jawaban2Button.setOnClickListener { handleAnswer(jawaban2Button.text.toString()) }

                            } else {
                                Toast.makeText(this@KuisActivity, "Kuis selesai!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    } else {
                        Log.e("KuisActivity", "Response tidak berhasil. Code: ${response.code()}, Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ArrayList<QuizQuestionResponse>>, t: Throwable) {
                    Log.e("KuisActivity", "Error: ${t.message}")
                }
            }
        )
    }

    private fun handleAnswer(selectedAnswer: String) {
        val isCorrect = selectedAnswer == jawabanBenar

        val answerRequest = AnswerRequest(
            id_user = getUserSessionId(),
            id_kategori = idKategori,
            id_quiz = idPertanyaan,
            jawaban = selectedAnswer,
            isCorrect = isCorrect
        )
        Log.d("KuisActivity", "AnswerRequest: $answerRequest")

        RetrofitClient.instance.postAnswer(answerRequest).enqueue(
            object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        if (idQuiz == 10) {
                            val intentReview = Intent(this@KuisActivity, KuisReviewActivity::class.java)
                            intentReview.putExtra("id_kategori", idKategori)
                            intentReview.putExtra("id_user", getUserSessionId())
                            startActivity(intentReview)
                            finish()
                        } else {
                            idQuiz += 1
                            loadQuiz()
                        }
                    } else {
                        Toast.makeText(this@KuisActivity, "Gagal mengirim jawaban!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@KuisActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun getUserSessionId(): String {
        val sharedPreferences = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        return sharedPreferences.getString("ID_USER", "") ?: ""
    }

}