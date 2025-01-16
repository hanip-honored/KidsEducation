package com.example.kidseducation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.collection.UserCollectionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CollectionListActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_collection_list)

        val sharedPreferences = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("ID_USER", null)
        val idKategoriObjek = intent.getStringExtra("id_kategori_objek")

        val RVKoleksi: RecyclerView = findViewById(R.id.recyclerViewCollectionList)
        RVKoleksi.layoutManager = GridLayoutManager(this, 1)

        if (idUser.isNullOrEmpty() || idKategoriObjek.isNullOrEmpty()) {
            Toast.makeText(this, "Data pengguna atau kategori tidak valid.", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.instance.getUserCollection(idUser, idKategoriObjek).enqueue(
            object : Callback<ArrayList<UserCollectionResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<UserCollectionResponse>>,
                    response: Response<ArrayList<UserCollectionResponse>>
                ) {
                    if (response.isSuccessful) {
                        val listCollection = response.body() ?: arrayListOf()
                        val adapter = AdapterCollectionList(listCollection)
                        RVKoleksi.adapter = adapter
                    } else {
                        Log.e("CollectionListActivity", "Response gagal: ${response.message()}")
                        Toast.makeText(this@CollectionListActivity, "Gagal memuat data koleksi.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserCollectionResponse>>, t: Throwable) {
                    Log.e("CollectionListActivity", "Error: ${t.message}")
                    Toast.makeText(this@CollectionListActivity, "Terjadi kesalahan saat memuat data.", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}