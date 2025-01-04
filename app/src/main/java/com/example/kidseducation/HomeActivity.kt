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

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTrx = fragmentManager.beginTransaction()
        fragmentTrx.replace(R.id.fragmentContainerView, fragment)
        fragmentTrx.commit()
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val idUser = intent.getStringExtra("ID_USER")

        val homeFragment = HomeFragment()
        val bundle = Bundle()
        bundle.putString("ID_USER", idUser)
        homeFragment.arguments = bundle

        replaceFragment(homeFragment)
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