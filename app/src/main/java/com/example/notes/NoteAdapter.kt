package com.example.notes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class NoteAdapter(private val list: ArrayList<Note>, private val tvNoNotes: TextView): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("notes")
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvDateTime: TextView = view.findViewById(R.id.tvDateTime)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val ivEdit: ImageView = view.findViewById(R.id.ivEdit)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_each_note, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = list[position]
        holder.tvDateTime.text = note.noteDateTime
        if (note.title.length <= 18) {
            holder.tvTitle.text = note.title
        } else {
            val title = note.title.substring(0, 19)
            holder.tvTitle.text = "${title}..."
        }

        if (note.title.length <= 30) {
            holder.tvContent.text = note.content
        } else {
            val content = note.content.substring(0, 31)
            holder.tvContent.text = "${content}..."
        }

        holder.ivDelete.setOnClickListener {
            databaseReference.child(note.id).removeValue()
            list.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            if (list.isEmpty()) {
                tvNoNotes.visibility = View.VISIBLE
            }
        }
    }
}