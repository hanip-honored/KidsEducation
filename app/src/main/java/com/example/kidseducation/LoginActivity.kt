package com.example.kidseducation

import android.app.ProgressDialog
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private  lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val btnRegister = findViewById<TextView>(R.id.noAccount)
        val btnLogin = findViewById<Button>(R.id.loginButton)

        btnLogin.setOnClickListener {
            if (email.text.length > 0 && password.text.length > 0) {
                login(email.text.toString(), password.text.toString())
            } else {
                Toast.makeText(applicationContext, "Silahkan isi semua data!", Toast.LENGTH_SHORT).show()
            }
        }

        val textRegister = "Belum punya akun? Daftar disini"

        // Membuat SpannableString
        val spannableString = SpannableString(textRegister)
        // Mengatur bagian "Register" bisa diklik
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Aksi ketika "Register" diklik, bisa diarahkan ke activity lain
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        // Menandai bagian "Register" bisa diklik
        spannableString.setSpan(clickableSpan, 18, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        btnRegister.text = spannableString
        btnRegister.movementMethod = LinkMovementMethod.getInstance()

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Silahkan Tunggu")
        progressDialog.setCancelable(false)

        // Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set up button click listeners
        findViewById<Button>(R.id.googleSignInButton).setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null){
                if (task.result.user != null){
                    reload()
                } else {
                    Toast.makeText(applicationContext, "Login Gagal", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Login Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithGoogle() {
        // Sign out terlebih dahulu untuk memastikan picker muncul
        googleSignInClient.signOut().addOnCompleteListener(this) {
            // Revoking access untuk memastikan picker muncul
            googleSignInClient.revokeAccess().addOnCompleteListener {
                // Setelah sign out dan revoke, baru mulai proses sign in
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        this,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Add flags to clear the back stack
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    private fun reload(){
        startActivity(Intent(applicationContext, HomeActivity::class.java))
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    companion object {
        private const val RC_SIGN_IN = 2990
        private const val TAG = "LoginActivity"
    }
}