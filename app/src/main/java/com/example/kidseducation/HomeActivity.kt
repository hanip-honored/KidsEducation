package com.example.kidseducation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.kidseducation.databinding.ActivityHomeBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapePath
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth


class HomeActivity : AppCompatActivity() {
    private  lateinit var firebaseUser: FirebaseUser

    private lateinit var binding: ActivityHomeBinding

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val idUser = sharedPreferences.getString("ID_USER", "Unknown")
        val username = sharedPreferences.getString("USERNAME", "Unknown")
        val level = sharedPreferences.getInt("LEVEL", 1)

        val bundle = Bundle().apply {
            putString("ID_USER", idUser)
            putString("USERNAME", username)
            putString("LEVEL", level.toString())
        }

        replaceFragment(HomeFragment(), bundle)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.home -> HomeFragment()
                R.id.book -> CollectionFragment()
                R.id.timer -> ProgressFragment()
                R.id.user -> ProfileFragment()
                else -> return@setOnNavigationItemSelectedListener false
            }
            replaceFragment(fragment, bundle)
            true
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("ID_USER", idUser)
            startActivity(intent)
        }

        // Menangani klik pada tombol scan
//        findViewById<TextView>(R.id.txtMenuScan).setOnClickListener {
//            val intent = Intent(this, ScanActivity::class.java)
//            intent.putExtra("ID_USER", idUser)
//            startActivity(intent)
//        }
//
//        findViewById<TextView>(R.id.txtMenuScan2).setOnClickListener {
//            val intent = Intent(this, ScanActivity::class.java)
//            intent.putExtra("ID_USER", idUser)
//            startActivity(intent)
//        }

//
//        val txtHome: TextView = findViewById(R.id.home)
//        txtHome.setOnClickListener {
//            replaceFragment(HomeFragment())
//        }

//        val textName = findViewById<TextView>(R.id.name)



//        firebaseUser = FirebaseAuth.getInstance().currentUser!!
//
//        if (firebaseUser.displayName != null){
//            textName?.setText(firebaseUser.displayName)
//        } else {
//            textName.setText("Login Gagal")
//        }
//
//        findViewById<Button>(R.id.logout).setOnClickListener {
//            Firebase.auth.signOut()
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
    }
}