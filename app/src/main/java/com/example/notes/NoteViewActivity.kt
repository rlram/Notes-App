package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NoteViewActivity : AppCompatActivity() {
    private lateinit var ivBack: ImageView
    private lateinit var tvNoteTitle: TextView
    private lateinit var tvNoteContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ivBack = findViewById(R.id.ivBackNoteView)
        tvNoteTitle = findViewById(R.id.tvNoteTitle)
        tvNoteContent = findViewById(R.id.tvNoteContent)

        ivBack.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")

        tvNoteTitle.text = title
        tvNoteContent.text = content
    }
}