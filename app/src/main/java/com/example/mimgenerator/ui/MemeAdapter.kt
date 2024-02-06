package com.example.mimgenerator.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mimgenerator.R
import com.example.mimgenerator.databinding.ItemMemeCardBinding
import com.example.mimgenerator.domain.model.Meme

class MemeAdapter: RecyclerView.Adapter<MemeAdapter.FilmViewHolder>() {

    private var onItemClick: ((Meme) -> Unit)? = null
    private var listData = ArrayList<Meme>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newListData: List<Meme>) {
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Meme) -> Unit) {
        onItemClick = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeAdapter.FilmViewHolder  =
        FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_meme_card, parent, false))

    override fun onBindViewHolder(holder: MemeAdapter.FilmViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMemeCardBinding.bind(itemView)
        fun bind(data: Meme){
            with(binding){
                Glide.with(this.root.context).load(data.url).into(this.ivMeme)
                root.setOnClickListener {
                    onItemClick?.invoke(data)
                }
            }
        }
    }
}