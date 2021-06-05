package com.eyeoh.androidquizdekd.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eyeoh.androidquizdekd.Data.InfoDao
import com.eyeoh.androidquizdekd.R
import java.text.SimpleDateFormat
import java.util.*

class InfoRecyclerViewAdapter(val context: Context, val list: ArrayList<InfoDao>) :
        RecyclerView.Adapter<InfoRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_info, parent, false)

        return InfoRecyclerViewAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position, list[position], holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(position: Int, data: InfoDao, context: Context) {

            val image_info: ImageView = itemView.findViewById(R.id.image_info)
            val txt_header: TextView = itemView.findViewById(R.id.txt_header)
            val txt_detail: TextView = itemView.findViewById(R.id.txt_detail)
            val txt_date: TextView = itemView.findViewById(R.id.txt_date)

            Glide.with(context)
                    .load(data.image_url.thumb)
                    .fitCenter()
                    .placeholder(R.drawable.image_test)
                    .into(image_info)

            txt_header.text = data.title
            txt_detail.text = data.description
            txt_date.text = data.createdAt

        }
    }

}