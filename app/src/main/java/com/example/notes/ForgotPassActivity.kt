package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var etEmailAddress: EditText
    private lateinit var btnSend: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_pass)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etEmailAddress = findViewById(R.id.etTextEmailAddressForgotPass)
        btnSend = findViewById(R.id.btnSendForgotPass)

        firebaseAuth = Firebase.auth

        btnSend.setOnClickListener {
            val email = etEmailAddress.text.toString()
            if (email.isEmpty()) {
                etEmailAddress.error = "Email is required"
            }
            if (email.isNotEmpty()) {
                sendResetLink(email)
            }
        }
    }

    private fun sendResetLink(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    val intent = Intent(applicationContext, SignInActivity::class.java)
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