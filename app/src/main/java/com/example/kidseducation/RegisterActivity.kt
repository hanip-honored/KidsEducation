package com.example.kidseducation

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kidseducation.client.RetrofitClient
import com.example.kidseducation.response.account.RegisterRequest
import com.example.kidseducation.response.account.RegisterResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
//    private lateinit var auth: FirebaseAuth
//    private  lateinit var progressDialog: ProgressDialog
//
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val nickname = intent.getStringExtra("NICKNAME")
        val btnRegister = findViewById<Button>(R.id.buttonRegister)
        val email = findViewById<EditText>(R.id.TextEmail)
        val password = findViewById<EditText>(R.id.TextPassword)

//        auth = FirebaseAuth.getInstance()
//        progressDialog = ProgressDialog(this)
//        progressDialog.setTitle("Loading")
//        progressDialog.setMessage("Silahkan Tunggu")
//        progressDialog.setCancelable(false)


    btnRegister.setOnClickListener {
        val nicknameText = nickname ?: ""
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()

        if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
            val request = RegisterRequest(nicknameText, emailText, passwordText)

            RetrofitClient.instance.registerUser(request).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(applicationContext, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()

                        val intentLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intentLogin)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Registrasi gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(applicationContext, "Silahkan isi semua data!", Toast.LENGTH_SHORT).show()
        }
    }


}
//
//
//        val textLogin = "Sudah memiliki akun? Login"
//
//        // Membuat SpannableString
//        val spannableString = SpannableString(textLogin)
//
//        // Mengatur bagian "Register" bisa diklik
//        val clickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                // Aksi ketika "Register" diklik, bisa diarahkan ke activity lain
//                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                startActivity(intent)
//            }
//        }
//
//        // Menandai bagian "Register" bisa diklik
//        spannableString.setSpan(clickableSpan, 21, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//        btnLogin.text = spannableString
//        btnLogin.movementMethod = LinkMovementMethod.getInstance()
//    }
//
//    private fun register(name: String, email: String, password: String){
//        progressDialog.show()
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
//            if (task.isSuccessful && task.getResult() != null){
//                val user = task.getResult().user
//
//                if (user != null){
//                    val profileUpdates = userProfileChangeRequest {
//                        displayName = name
//                        photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
//                    }
//
//                    user!!.updateProfile(profileUpdates)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Log.d(TAG, "User profile updated.")
//                                reload()
//                            }
//                        }
//                } else {
//                    Toast.makeText(applicationContext, "Register Gagal", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                Toast.makeText(
//                    baseContext,
//                    "Authentication failed.",
//                    Toast.LENGTH_SHORT,
//                ).show()
//                updateUI(null)i
//            }
//        }
//    }
//
//    private fun reload(){
//        startActivity(Intent(applicationContext, HomeActivity::class.java))
//    }
//
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            reload()
//        }
//    }
}