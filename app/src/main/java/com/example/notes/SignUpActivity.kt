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

class SignUpActivity : AppCompatActivity() {
    private lateinit var etEmailAddress: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSignUp: Button
    private lateinit var tvSignIn: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etEmailAddress = findViewById(R.id.etTextEmailAddressSignUp)
        etPassword = findViewById(R.id.etTextPasswordSignUp)
        etConfirmPassword = findViewById(R.id.etTextConfirmPasswordSignUp)
        progressBar = findViewById(R.id.progressBarSignUp)
        btnSignUp = findViewById(R.id.btnSignUpSignUp)
        tvSignIn = findViewById(R.id.tvSignInSignUp)

        firebaseAuth = Firebase.auth

        tvSignIn.setOnClickListener {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignUp.setOnClickListener {
            val email = etEmailAddress.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
                etEmailAddress.error = "Email is required"
                etPassword.error = "Password is required"
                etConfirmPassword.error = "Confirm password is required"
            }
            if (email.isEmpty()) etEmailAddress.error = "Email is required"
            if (password.isEmpty()) etPassword.error = "Password is required"
            if (confirmPassword.isEmpty()) etConfirmPassword.error = "Confirm password is required"

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    signUp(email, password)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Password didn't match",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        }
    }

    private fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
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

}