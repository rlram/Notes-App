package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {
    private lateinit var ivSignOut: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNoNotes: TextView
    private lateinit var btnCreateNotes: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: NoteAdapter

    private val noteList = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ivSignOut = findViewById(R.id.ivSignOut)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBarMain)
        tvNoNotes = findViewById(R.id.tvNoNotes)
        btnCreateNotes = findViewById(R.id.btnCreateNotes)

        firebaseAuth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("notes")

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = NoteAdapter(noteList, tvNoNotes, this)
        recyclerView.adapter = adapter

        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    progressBar.visibility = View.GONE
                    noteList.clear()
                    for (item in snapshot.children) {
                        val uId = firebaseAuth.currentUser?.uid.toString()
                        if (item.child("userId").value.toString() == uId) {
                            val id = item.child("id").value.toString()
                            val title = item.child("title").value.toString()
                            val content = item.child("content").value.toString()
                            val noteDateTime = item.child("noteDateTime").value.toString()
                            val userId = item.child("userId").value.toString()
                            noteList.add(Note(id, title, content, noteDateTime, userId))
                            adapter.notifyItemChanged(noteList.size)
                        }
                    }
                } else {
                    progressBar.visibility = View.GONE
                    tvNoNotes.visibility = View.VISIBLE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    error.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        ivSignOut.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCreateNotes.setOnClickListener {
            val intent = Intent(applicationContext, AddNoteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}