package com.example.kidseducation

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.collection.SaveResponse
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

class ObjectDetectionActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_detection)

        val sharedPreferences = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("ID_USER", null)

        val buttonGallery = findViewById<Button>(R.id.button_gallery)
        val back = findViewById<ImageView>(R.id.circle_back)

        buttonGallery.setOnClickListener {
            openGallery(idUser)
        }

        back.setOnClickListener {
            val intentKuis = Intent(this, HomeActivity::class.java)
            startActivity(intentKuis)
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            processImage(bitmap)
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery(idUser: String?) {
        galleryLauncher.launch("image/*")
    }

    private fun processImage(bitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE) // Untuk gambar statis
            .enableMultipleObjects()
            .enableClassification()
            .build()

        val objectDetector = ObjectDetection.getClient(options)

        objectDetector.process(inputImage)
            .addOnSuccessListener { detectedObjects ->
                if (detectedObjects.isNotEmpty()) {
                    val label = detectedObjects.first().labels.firstOrNull()?.text ?: "Unknown"
                    fetchWikipediaDescription(label)
                } else {
                    Toast.makeText(this, "No object detected", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Detection failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchWikipediaDescription(objectName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/api/rest_v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(WikipediaApiService::class.java)
        val call = api.getObjectInfo(objectName)

        call.enqueue(object : Callback<WikipediaResponse> {
            override fun onResponse(
                call: Call<WikipediaResponse>,
                response: Response<WikipediaResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val description = result?.extract ?: "Description not available"
                    val imageUrl = result?.thumbnail?.source

                    val sharedPreferences = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
                    val idUser = sharedPreferences.getString("ID_USER", null)

                    if (idUser != null) {
                        navigateToDetail(idUser, objectName, description, imageUrl)
                    } else {
                        Toast.makeText(this@ObjectDetectionActivity, "User ID not found.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ObjectDetectionActivity, "No information found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WikipediaResponse>, t: Throwable) {
                Toast.makeText(this@ObjectDetectionActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToDetail(idUser: String, objectName: String, description: String, imageUrl: String?) {
        if (imageUrl != null) {
            saveToDatabase(idUser, objectName, description, imageUrl)
        }

        val intent = Intent(this, ObjectDetailActivity::class.java)
        intent.putExtra("object_name", objectName)
        intent.putExtra("object_description", description)
        intent.putExtra("object_image_url", imageUrl ?: "https://via.placeholder.com/150")
        startActivity(intent)
    }

    private fun saveToDatabase(idUser: String, objectName: String, description: String, imageUrl: String) {
        RetrofitClient.instance.saveObject(idUser, objectName, description, imageUrl)
            .enqueue(object : Callback<SaveResponse> {
                override fun onResponse(call: Call<SaveResponse>, response: Response<SaveResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result?.success == true) {
                            Toast.makeText(this@ObjectDetectionActivity, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@ObjectDetectionActivity, "Failed to save data: ${result?.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ObjectDetectionActivity, "Failed to save data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SaveResponse>, t: Throwable) {
                    Log.e("ObjectDetectionActivity", "Error: ${t.message}")
                    Toast.makeText(this@ObjectDetectionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    interface WikipediaApiService {
        @GET("page/summary/{title}")
        fun getObjectInfo(@Path("title") title: String): Call<WikipediaResponse>
    }

    data class WikipediaResponse(
        val title: String,
        val extract: String,
        val thumbnail: Thumbnail?
    )

    data class Thumbnail(
        val source: String
    )
}