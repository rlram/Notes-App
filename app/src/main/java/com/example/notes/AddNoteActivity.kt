package com.example.notes

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNoteActivity : AppCompatActivity() {
    private lateinit var ivBack: ImageView
    private lateinit var btnSave: Button
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_note)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ivBack = findViewById(R.id.ivBack)
        btnSave = findViewById(R.id.btnSave)
        etTitle = findViewById(R.id.etTextTitleNote)
        etContent = findViewById(R.id.etTextContentNote)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("notes")
        firebaseAuth = Firebase.auth

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            if (title.isEmpty() && content.isEmpty()) {
                etTitle.error = "Title is required"
                etContent.error = "Content is required"
            }
            if (title.isEmpty()) etTitle.error = "Title is required"
            if (content.isEmpty()) etContent.error = "Content is required"

            if (title.isNotEmpty() && content.isNotEmpty()) {
                createNote(title, content)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNote(title: String, content: String) {
        val id = System.currentTimeMillis().toString()
        val uId = firebaseAuth.currentUser?.uid.toString()
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm")
        val formattedDateTime = currentDateTime.format(formatter).toString()
        val note = Note(id, title, content, formattedDateTime, uId)
        databaseReference.child(id).setValue(note)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
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