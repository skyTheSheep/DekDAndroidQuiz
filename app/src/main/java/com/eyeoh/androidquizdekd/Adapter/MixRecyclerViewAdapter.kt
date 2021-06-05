package com.eyeoh.androidquizdekd.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eyeoh.androidquizdekd.Activity.InfoActivity
import com.eyeoh.androidquizdekd.Data.BannerDao
import com.eyeoh.androidquizdekd.Data.InfoDao
import com.eyeoh.androidquizdekd.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MixRecyclerViewAdapter(val context: Context,val bannerList: ArrayList<BannerDao>,val infoList: ArrayList<InfoDao>, val clickListener: OnRecyclerItemClick) :
        RecyclerView.Adapter<MixRecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mix, parent, false)

        return MixRecyclerViewAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var infoPosition = position - 1
        if (infoPosition < 0) {
            infoPosition = 0
        }
        if (infoList.size > 0) {
            holder.bindItems(position, bannerList, infoList[infoPosition], holder.itemView.context, clickListener)
        }

    }

    override fun getItemCount(): Int {
        return infoList.size + 1
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(position: Int, bannerData: ArrayList<BannerDao> , infoData: InfoDao, context: Context, clickListener: OnRecyclerItemClick) {

            val ll_banner: LinearLayout = itemView.findViewById(R.id.ll_banner)
            val cv_info: CardView = itemView.findViewById(R.id.cv_info)

            if (position == 0) { // Banner Part
                val rv_banner_items: RecyclerView = itemView.findViewById(R.id.rv_banner_items)
                rv_banner_items.apply {
                    val bannerAdapter = BannerRecyclerViewAdapter(context,bannerData)
                    val rvBanner = findViewById<RecyclerView>(R.id.rv_banner_items)
                    rvBanner.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    rvBanner.adapter = bannerAdapter
                }
                ll_banner.visibility = View.VISIBLE
                cv_info.visibility = View.GONE
            } else { // info Part
                val image_info: ImageView = itemView.findViewById(R.id.image_info)
                val txt_header: TextView = itemView.findViewById(R.id.txt_header)
                val txt_detail: TextView = itemView.findViewById(R.id.txt_detail)
                val txt_date: TextView = itemView.findViewById(R.id.txt_date)

                Glide.with(context)
                        .load(infoData.image_url.thumb)
                        .fitCenter()
                        .placeholder(R.drawable.image_test)
                        .into(image_info)

                txt_header.text = infoData.title
                txt_detail.text = infoData.description
                txt_date.text = dateConvert(infoData.createdAt)

                ll_banner.visibility = View.GONE
                cv_info.visibility = View.VISIBLE

                cv_info.setOnClickListener {
                    clickListener.onItemClickListener(infoData.id)
                }
            }
        }

        fun dateConvert(date: String): String {
            val utc = TimeZone.getTimeZone("UTC")
            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val destFormat = SimpleDateFormat("yyyy-MM-dd-HH:mm")
            sourceFormat.timeZone = utc
            val convertedDate : Date = sourceFormat.parse(date)
            val result = destFormat.format(convertedDate)
            val list = result.split("-")

            val monthList: MutableList<String> = mutableListOf("ม.ค.","ก.พ.","ม.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค.")

            var year = list[0].toInt() + 543
            var yearInString = year.toString().substring(2,4)
            var month = list[1].toInt() - 1
            var monthInString = monthList[month]
            var day = list[2].toInt()
            var time = list[3]
            return "วันที่สร้าง $day $monthInString $yearInString / $time น."
        }
    }

    interface  OnRecyclerItemClick {
        fun onItemClickListener(id: Int)
    }
}