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

    private fun replaceFragment(fragment: Fragment, bundle: Bundle){
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

        val idUser = intent.getStringExtra("ID_USER")
        val username = intent.getStringExtra("USERNAME")

        val bundle = Bundle()
        bundle.putString("ID_USER", idUser)
        bundle.putString("USERNAME", username)

        // Menampilkan fragmen awal
        replaceFragment(HomeFragment(), bundle)

        // Menangani klik pada menu navigasi
        findViewById<TextView>(R.id.txtMenuHome).setOnClickListener {
            replaceFragment(HomeFragment(), bundle)
        }

        findViewById<TextView>(R.id.txtMenuBook).setOnClickListener {
            replaceFragment(CollectionFragment(), bundle)
        }

        findViewById<TextView>(R.id.txtMenuTimer).setOnClickListener {
            replaceFragment(ProgressFragment(), bundle)
        }

        findViewById<TextView>(R.id.txtMenuUser).setOnClickListener {
            replaceFragment(ProfileFragment(), bundle)
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