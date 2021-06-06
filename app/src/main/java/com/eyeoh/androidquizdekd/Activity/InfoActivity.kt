package com.eyeoh.androidquizdekd.Activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.eyeoh.androidquizdekd.Data.BaseJSONObject
import com.eyeoh.androidquizdekd.Data.InfoDao
import com.eyeoh.androidquizdekd.R
import com.eyeoh.androidquizdekd.Service.HttpManager
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_info.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class InfoActivity : AppCompatActivity() {

    private var infoID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        setSupportActionBar(findViewById(R.id.info_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        infoID = intent?.extras?.getInt("id")!!

        getInfo(infoID)
    }

    private fun initView(info: InfoDao) {
        val imageInfo: ImageView = findViewById(R.id.image_info)
        val tvInfo: TextView = findViewById(R.id.tv_info)
        val tvDateInfo: TextView = findViewById(R.id.tv_date_info)

        Glide.with(this)
                .load(info.image_url.cover_image)
                .placeholder(R.drawable.image_test)
                .into(imageInfo)

        tvInfo.text = info.description
        tvDateInfo.text = dateConvert(info.createdAt)
        tv_title.text = info.title
    }

    private fun getInfo(id: Int) {

        showProgressDialog()

        HttpManager.service.getItemListInfo(id).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                dismissProgressDialog()
                try {
                    if (response.isSuccessful) {
                        Log.e("checkApi","getInfo is Successful")
                        val result = response.body()?.string()
                        val json = BaseJSONObject(result)

                        val infoItem = InfoDao(json)

                        initView(infoItem)

                    } else {
                        Log.e("checkApi","getInfo: Connection response is not success : ${response.message()}")
                    }

                } catch (e: Exception) {
                    Log.e("checkApi","getInfo: Connection Failed: ${e.message}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
                Log.e("checkApi","getInfo: Connection on Failed: ${t.message}")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
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

    var progressDialog: KProgressHUD? = null
    fun showProgressDialog() {
        try {
            dismissProgressDialog()

            progressDialog = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
            progressDialog!!.show()
        } catch (e: Exception) {
        }

    }

    fun dismissProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog!!.dismiss()
                progressDialog = null
            }
        } catch (e: Exception) {
        }
    }
}