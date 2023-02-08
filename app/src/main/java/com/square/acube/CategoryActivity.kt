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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.square.acube.adapter.SectionsAdapter
import com.square.acube.databinding.ActivityCategoryBinding
import com.square.acube.model.category.CategoryResponse
import com.square.acube.model.dashboard.Section
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestConstants
import com.square.acube.network.RestController
import com.square.acube.utils.Constants

class CategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryBinding
    private var ID: String? = ""
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
        binding= ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ID = intent.getStringExtra(Constants.ID)

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

        binding.backButton.setOnClickListener { finish() }
        binding.refreshLayout.setOnRefreshListener { getCategory(ID!!) }
        getCategory(ID!!)
    }

    private fun getCategory(id:String) {
        binding.refreshLayout.isRefreshing = true
        val headerModel = HeaderModel()
        RestController(headerModel).getCategoryDetails(this, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as CategoryResponse
                Log.e("getCategoryOnResponse: ", response.toString())
                val banner =response.banner
                var sections: ArrayList<Section> = ArrayList();
                val imageList = ArrayList<SlideModel>()
                sections.addAll(response.section)
                response.banner.forEach {
                    imageList.add(SlideModel(RestConstants.BANNER_PATH+it.image,  ScaleTypes.CENTER_CROP))
                }
                binding.imageSlider.setImageList(imageList)
                binding.imageSlider.setItemClickListener(object : ItemClickListener {
                    override fun onItemSelected(position: Int) {
                        val intent = Intent(this@CategoryActivity, DetailsActivity::class.java)
                        intent.putExtra(Constants.ID, banner[position].videoid)
                        startActivity(intent)
                    }
                })
                val sectionsRcv: RecyclerView = binding.sectionsRcv
                var sectionsAdapter = SectionsAdapter(this@CategoryActivity, sections)
                sectionsAdapter.onItemClick = { position, videoPosition ->
                    val intent = Intent(this@CategoryActivity, DetailsActivity::class.java)
                    intent.putExtra(Constants.ID, sections[position].video[videoPosition].id)
                    startActivity(intent)
                }
                sectionsAdapter.onViewAllClick = { position ->
                    val intent = Intent(this@CategoryActivity, ViewAllActivity::class.java)
                    intent.putExtra(Constants.TITLE, sections[position].title)
                    intent.putExtra(Constants.SECTIONID, sections[position].id)
                    startActivity(intent)
                }
                sectionsRcv.layoutManager = LinearLayoutManager(this@CategoryActivity)
                sectionsRcv.adapter = sectionsAdapter
                binding.refreshLayout.isRefreshing = false
            }
            override fun onFailure(t: Any?) {
                binding.refreshLayout.isRefreshing = false
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
                    getCategory(id)
                }

            }
        }, id)
    }

}