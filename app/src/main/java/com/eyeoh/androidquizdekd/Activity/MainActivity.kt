package com.eyeoh.androidquizdekd.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eyeoh.androidquizdekd.Adapter.BannerRecyclerViewAdapter
import com.eyeoh.androidquizdekd.Adapter.InfoRecyclerViewAdapter
import com.eyeoh.androidquizdekd.Adapter.MixRecyclerViewAdapter
import com.eyeoh.androidquizdekd.Data.BannerDao
import com.eyeoh.androidquizdekd.Data.BaseJSONObject
import com.eyeoh.androidquizdekd.Data.InfoDao
import com.eyeoh.androidquizdekd.R
import com.eyeoh.androidquizdekd.Service.HttpManager
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MixRecyclerViewAdapter.OnRecyclerItemClick {

    private var bannerList: ArrayList<BannerDao> = ArrayList()
    private var infoList: ArrayList<InfoDao> = ArrayList()

    private var totalItem = 0
    private var lastPage = false
    private var isLoadingAPI = false
    private var mode = 1
    private var NO_SORT = 1
    private var SORT_BY_DATE_DESC = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.title = "Product"
        initView()
        initAdapter()
        getBanner()
    }

    private fun initView() {
        Thread {
            Glide.get(applicationContext).clearDiskCache()
        }.start()

        btn_sorting.setOnClickListener {
            when(mode) {
                NO_SORT -> {
                    getSortedList(true)
                    mode = SORT_BY_DATE_DESC
                }
                SORT_BY_DATE_DESC -> {
                    getList(true)
                    mode = NO_SORT
                }
            }
        }
    }

    private fun initAdapter() {

        val mixAdapter = MixRecyclerViewAdapter(this, bannerList, infoList,this@MainActivity)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_info_items.layoutManager = layoutManager
        rv_info_items.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override val totalPageCount: Int
                get() = infoList.size / 20
            override val isLastPage: Boolean
                get() = lastPage
            override val isLoading: Boolean
                get() = isLoadingAPI

            override fun loadMoreItems() {
                progressbar.visibility = View.VISIBLE
//                getList(false, totalPageCount + 1)
                when(mode) {
                    NO_SORT -> {
                        getList(false, totalPageCount + 1)
                    }
                    SORT_BY_DATE_DESC -> {
                        getSortedList(false, totalPageCount + 1)
                    }
                }
            }

        })
        rv_info_items.adapter = mixAdapter

    }

    private fun getBanner() {

        if (isLoadingAPI) {
            return
        }

        isLoadingAPI = true

        showProgressDialog()

        if (bannerList.isEmpty()) {
            HttpManager.service.getBanner().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    dismissProgressDialog()
                    try {
                        if (response.isSuccessful) {
                            val result = response.body()!!.string()
                            val jsonArray = JSONArray(result)

                            for (item in 0 until jsonArray.length()) {
                                try {
                                    bannerList.add(BannerDao(BaseJSONObject(jsonArray.getString(item))))
                                } catch (e: Exception) {
                                    Log.e("checkApi","getBanner: adding item error")
                                }
                            }

                            for (item in bannerList) {
                                Log.e("checkList","id: ${item.id} imageUrl: ${item.imageUrl}")
                            }

                            isLoadingAPI = false

                            getList(true)


                        } else {
                            Log.e("checkApi","getBanner: Connection response is not success : ${response.message()}")
                        }
                    } catch (e: Exception) {
                        Log.e("checkApi","getBanner: Connection Failed: ${e.message}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    isLoadingAPI = false
                    dismissProgressDialog()
                    Log.e("checkApi","getBanner: Connection on Failed: ${t.message}")
                }

            })
        }

    }

    private fun getList(isReset: Boolean = false, page: Int = 1) {
        Log.e("checkTotal","$totalItem")

        if (isLoadingAPI) {
            return
        }

        isLoadingAPI = true

        if (isReset) {
            showProgressDialog()
        }


        HttpManager.service.getItemList(page).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (isReset) {
                    dismissProgressDialog()
                }
                try {
                    if (response.isSuccessful) {
                        Log.e("checkApi","getList is Successful")
                        val result = response.body()?.string()
                        val json = BaseJSONObject(result)
                        val jsonArray = json.getJSONArray("list")
                        totalItem = json.getInt("total")

                        Log.e("checkApi","Total: $totalItem")

                        if (isReset) {
                            infoList.clear()
                            lastPage = false
                        }

                        if (jsonArray.length() > 0) {
                            for (item in 0 until jsonArray.length()) {
                                infoList.add(InfoDao(BaseJSONObject(jsonArray.getString(item))))
                            }

                            for (item in infoList) {
                                Log.e("checkList","infoDao:${item}")
                            }
                        } else {
                            lastPage = true
                        }

                        if (infoList.size == totalItem) {
                            Log.e("checkTotal","Done:${infoList.size}")
                            lastPage = true
                        }

                        rv_info_items.adapter?.notifyDataSetChanged()

                        progressbar.visibility = View.GONE


                    } else {
                        Log.e("checkApi","getList: Connection response is not success : ${response.message()}")
                    }

                } catch (e: Exception) {
                    Log.e("checkApi","getList: Connection Failed: ${e.message}")
                }
                isLoadingAPI = false
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
                isLoadingAPI = false
                Log.e("checkApi","getList: Connection on Failed: ${t.message}")
            }

        })
    }

    private fun getSortedList(isReset: Boolean = false, page: Int = 1) {

        if (isLoadingAPI) {
            return
        }

        isLoadingAPI = true

        if (isReset) {
            showProgressDialog()
        }

        HttpManager.service.getsortedList("createdAt","desc",page,20).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (isReset) {
                    dismissProgressDialog()
                }
                try {
                    if (response.isSuccessful) {
                        Log.e("checkApi","getList is Successful")
                        val result = response.body()?.string()
                        val json = BaseJSONObject(result)
                        val jsonArray = json.getJSONArray("list")

                        if (isReset) {
                            infoList.clear()
                            lastPage = false
                        }

                        if (jsonArray.length() > 0) {
                            for (item in 0 until jsonArray.length()) {
                                infoList.add(InfoDao(BaseJSONObject(jsonArray.getString(item))))
                            }

                            Log.e("checkList","infoList size:${infoList.size}")

                            for (item in infoList) {
                                Log.e("checkList","infoDao:${item}")
                            }
                        } else {
                            lastPage = true
                        }

                        if (infoList.size == totalItem) {
                            Log.e("checkTotal","Done:${infoList.size}")
                            lastPage = true
                        }

                        rv_info_items.adapter?.notifyDataSetChanged()

                        progressbar.visibility = View.GONE

                    } else {
                        Log.e("checkApi","getList: Connection response is not success : ${response.message()}")
                    }

                } catch (e: Exception) {
                    Log.e("checkApi","getList: Connection Failed: ${e.message}")
                }
                isLoadingAPI = false
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
                isLoadingAPI = false
                Log.e("checkApi","getList: Connection on Failed: ${t.message}")
            }

        })
    }

    override fun onItemClickListener(id: Int) {
        val intent = Intent(this@MainActivity, InfoActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
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

    abstract inner class PaginationScrollListener(internal var layoutManager: LinearLayoutManager) :
        RecyclerView.OnScrollListener() {

        abstract val totalPageCount: Int

        abstract val isLastPage: Boolean

        abstract val isLoading: Boolean

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            Log.e("checkScroll","visibleItemCount:$visibleItemCount")
            Log.e("checkScroll","firstVisibleItemPosition:$firstVisibleItemPosition")
            Log.e("checkScroll","totalItemCount:$totalItemCount")

            if (!isLoading&&!isLastPage) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems()
                }
            }
        }

        protected abstract fun loadMoreItems()
    }


}