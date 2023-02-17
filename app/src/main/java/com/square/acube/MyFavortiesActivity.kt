package com.square.acube

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.square.acube.adapter.FavouriteAdapter
import com.square.acube.databinding.ActivityMyFavortiesBinding
import com.square.acube.model.favourite.AddFavourite
import com.square.acube.model.favourite.MyFavourite
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestController
import com.square.acube.utils.Constants

class MyFavortiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyFavortiesBinding
    private lateinit var topSeriesRcv: RecyclerView
    var userID: String = ""
    private lateinit var dialog: Dialog
    private lateinit var subTitle: TextView
    private lateinit var loadTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorImage: ImageView
    private lateinit var doneImage: ImageView
    private lateinit var yesButton: TextView
    private lateinit var noButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFavortiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("TOKEN_KEY", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("TOKEN_KEY", "").toString()

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.progress_layout)
        subTitle=dialog.findViewById(R.id.subTitle)
        loadTitle=dialog.findViewById(R.id.loadTitle)
        errorImage=dialog.findViewById(R.id.error)
        progressBar=dialog.findViewById(R.id.progress)
        yesButton=dialog.findViewById(R.id.yesButton)
        noButton=dialog.findViewById(R.id.noButton)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.setBackgroundDrawableResource(R.color.trans)
        dialog.window!!.attributes= layoutParams

        binding.refreshLayout.setOnRefreshListener { getMyFavourite("get",userID) }
        if(!userID.isNullOrEmpty()) {
            getMyFavourite("get",userID)
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        initView()
    }

    private fun initView() {
        binding.backButton.setOnClickListener { finish() }
        binding.searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getMyFavourite( method: String,phoneno:String) {
        loadTitle.text="Please Wait"
        subTitle.text="Loading"
        dialog.show()
        binding.refreshLayout.isRefreshing = true
        val headerModel = HeaderModel()
        RestController(headerModel).getMyFavourite(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                binding.refreshLayout.isRefreshing = false
                val response = t as MyFavourite
                val data =response.data
                Log.e("onResponsegetMyFavourite","$response")

                if (data.isNotEmpty()) {
                    binding.topSeriesRcv.layoutManager = LinearLayoutManager(applicationContext)
                    var favouriteAdapter = FavouriteAdapter(applicationContext, data)

                    favouriteAdapter.onCloseClick = {
                        val id = data[it].id
                        progressBar.visibility= View.VISIBLE
                        errorImage.visibility= View.INVISIBLE
                        loadTitle.text="Removing"
                        subTitle.text="Removing from Watch Later"
                        dialog.show()
                        yesButton.visibility= View.INVISIBLE
                        addFavourite("remove", id!!, userID)
                        Log.e("TAG", "onResponseremove: $id",)
                    }

                    favouriteAdapter.onItemClick = { videoPosition ->
                        val intent = Intent(this@MyFavortiesActivity, DetailsActivity::class.java)
                        intent.putExtra(Constants.ID, data[videoPosition].id)
                        startActivity(intent)
                    }
                    binding.topSeriesRcv.adapter = favouriteAdapter
                }
                else {
                    binding.textNoData.visibility= View.VISIBLE
                    binding.topSeriesRcv.visibility= View.GONE
                }
                dialog.dismiss()
            }

            override fun onFailure(t: Any?) {
                binding.refreshLayout.isRefreshing = false
                Log.e( "onFailure: ", t.toString())
                dialog.dismiss()
            }

            override fun onNetworkFailure() {
                binding.refreshLayout.isRefreshing = false
                progressBar.visibility= View.INVISIBLE
                errorImage.visibility= View.VISIBLE
                loadTitle.text="Network Issue"
                subTitle.text="Please check your internet connection"
                dialog.show()
                yesButton.visibility= View.VISIBLE
                yesButton.text="Retry"
                yesButton.setOnClickListener {
                    dialog.dismiss()
                    getMyFavourite("get",userID)
                }
            }


        },method, phoneno)
    }

    private fun addFavourite( method: String,videoid:String,phoneno:String) {
        /*val progressDialog =SweetAlertDialog(this@MyFavortiesActivity, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog.setTitleText("Loading")
        progressDialog.show()*/
        val headerModel = HeaderModel()
        RestController(headerModel).addFavourite(this, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as AddFavourite
                getMyFavourite("get",userID)
                Log.e("onResponsegetMyFalljlvourite", "$response")
                //progressDialog.dismiss()
            }

            override fun onFailure(t: Any?) {
                Log.e("onFailure: ", t.toString())
            }

            override fun onNetworkFailure() {

            }
        }, method, videoid, phoneno)
        dialog.dismiss()
    }
}