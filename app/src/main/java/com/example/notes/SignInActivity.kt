package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import org.w3c.dom.Text

class SignInActivity : AppCompatActivity() {
    private lateinit var etEmailAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvForgotPassword: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSignIn: Button
    private lateinit var tvSignUp: TextView
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etEmailAddress = findViewById(R.id.etTextEmailAddressSignIn)
        etPassword = findViewById(R.id.etTextPasswordSignIn)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        progressBar = findViewById(R.id.progressBarSignIn)
        btnSignIn = findViewById(R.id.btnSignInSignIn)
        tvSignUp = findViewById(R.id.tvSignUpSignIn)

        firebaseAuth = Firebase.auth
        tvSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignIn.setOnClickListener {
            val email = etEmailAddress.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                etEmailAddress.error = "Email is required"
                etPassword.error = "Password is required"
            }
            if (email.isEmpty()) etEmailAddress.error = "Email is required"
            if (password.isEmpty()) etPassword.error = "Password is required"

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            }

        }
    }

    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.VISIBLE
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}