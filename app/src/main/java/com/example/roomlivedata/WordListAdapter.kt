package com.example.roomlivedata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomlivedata.db.entity.Word

class WordListAdapter :
    RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {
    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
    }

    private var words = emptyList<Word>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.recyclerview_item,
            parent,
            false
        )
        return WordViewHolder(itemView)
    }

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = words[position]
        holder.wordItemView.text = current.word
    }

    fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }
}
