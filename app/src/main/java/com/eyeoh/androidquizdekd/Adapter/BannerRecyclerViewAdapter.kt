package com.eyeoh.androidquizdekd.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eyeoh.androidquizdekd.Data.BannerDao
import com.eyeoh.androidquizdekd.R


class BannerRecyclerViewAdapter(val context: Context, val list: List<BannerDao>) :
    RecyclerView.Adapter<BannerRecyclerViewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position, list[position], holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(position: Int, data: BannerDao, context: Context) {

            val imageBanner: ImageView = itemView.findViewById(R.id.image_banner)

            Glide.with(context)
                .load(data.imageUrl)
//                        + "?rand=" + System.currentTimeMillis().toString())
                .placeholder(R.drawable.image_test)
                .into(imageBanner)
        }
    }
}