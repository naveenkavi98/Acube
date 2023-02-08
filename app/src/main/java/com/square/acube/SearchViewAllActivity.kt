package com.square.acube

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.square.acube.adapter.SearchViewAllAdapter
import com.square.acube.databinding.ActivitySearchViewAllBinding
import com.square.acube.model.search.Video
import com.square.acube.model.searchviewall.searhViewAllResponce
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestController
import com.square.acube.utils.Constants

class SearchViewAllActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchViewAllBinding
    private lateinit var topSeriesRcv: RecyclerView
    private var video: ArrayList<Video> = ArrayList();
    var id :String=""
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
        binding= ActivitySearchViewAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        //topSeriesRcv = binding.topSeriesRcv
        //topSeriesRcv.adapter = GridAdapter()
        //topSeriesRcv.layoutManager = GridLayoutManager(this, 3)
        binding.backButton.setOnClickListener { finish() }
        binding.title.text = intent.getStringExtra(Constants.TITLE)
        id = intent.getStringExtra(Constants.SECTIONID).toString()
        Log.e("TAG", "onCreate:$id ", )
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
        binding.refreshLayout.setOnRefreshListener { searchViewAll(id) }
        searchViewAll(id)
        binding.searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchViewAll(id:String) {
        binding.refreshLayout.isRefreshing = true

        val headerModel = HeaderModel()
        RestController(headerModel).searchViewAll(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                binding.refreshLayout.isRefreshing = false
                val response = t as searhViewAllResponce
                Log.e("TAG", "searchViewAll: $response", )
                val video = response.video

                topSeriesRcv = binding.topSeriesRcv
                val viewAllAdapter = SearchViewAllAdapter(applicationContext,video)

                viewAllAdapter.onItemClick = { videoPosition ->
                    val intent = Intent(this@SearchViewAllActivity, DetailsActivity::class.java)
                    intent.putExtra(Constants.ID, video[videoPosition].id)
                    startActivity(intent)
                }
                binding.topSeriesRcv.layoutManager= GridLayoutManager(applicationContext, 3)
                binding.topSeriesRcv.adapter=viewAllAdapter

            }

            override fun onFailure(t: Any?) {
                binding.refreshLayout.isRefreshing = false
                Log.e( "onFailure: ", t.toString())
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
                    searchViewAll(id)
                }
            }
        },id)
    }
}