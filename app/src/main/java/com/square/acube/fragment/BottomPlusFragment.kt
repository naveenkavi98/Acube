package com.square.acube.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.square.acube.DetailsActivity
import com.square.acube.R
import com.square.acube.ViewAllActivity
import com.square.acube.adapter.ImageAdapter
import com.square.acube.adapter.MovieImageAdapter
import com.square.acube.adapter.SectionsAdapter
import com.square.acube.databinding.FragmentBottomPlusBinding
import com.square.acube.model.category.CategoryResponse
import com.square.acube.model.dashboard.Section
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestConstants
import com.square.acube.network.RestController
import com.square.acube.utils.Constants


class BottomPlusFragment : Fragment() {

    private lateinit var binding: FragmentBottomPlusBinding
    private lateinit var dialog: Dialog
    private lateinit var subTitle: TextView
    private lateinit var loadTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorImage: ImageView
    private lateinit var doneImage: ImageView
    private lateinit var yesButton: TextView
    private lateinit var noButton: TextView
    private lateinit var handler : Handler
    private lateinit var imageList:ArrayList<String>
    private lateinit var adapter: MovieImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomPlusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        handler = Handler(Looper.myLooper()!!)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 2000)
            }
        })

        dialog = Dialog(requireContext())
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

        binding.refreshLayout.setOnRefreshListener { getCategory("3") }
        getCategory("3")
        return root
    }

    private fun getCategory(id:String) {
        binding.refreshLayout.isRefreshing = true
        val headerModel = HeaderModel()
        RestController(headerModel).getCategoryDetails(context, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as CategoryResponse
                Log.e("\n\n\n\n\nonResponse: ", response.toString())
                val banner =response.banner
                var sections: ArrayList<Section> = ArrayList();
                /*val imageList = ArrayList<SlideModel>()
                sections.addAll(response.section)
                response.banner.forEach {
                    imageList.add(SlideModel(RestConstants.BANNER_PATH+it.image,  ScaleTypes.CENTER_CROP))
                }
                binding.imageSlider.setImageList(imageList)
                binding.imageSlider.setItemClickListener(object : ItemClickListener {
                    override fun onItemSelected(position: Int) {
                        val intent = Intent(activity, DetailsActivity::class.java)
                        intent.putExtra(Constants.ID, banner[position].videoid)
                        startActivity(intent)
                    }
                })*/
                adapter = MovieImageAdapter(requireContext(),response.banner,  binding.viewPager)
                binding.viewPager.adapter = adapter
                binding.viewPager.offscreenPageLimit = 3
                binding.viewPager.clipToPadding = false
                binding.viewPager.clipChildren = false
                binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
                val transformer = CompositePageTransformer()
                transformer.addTransformer(MarginPageTransformer(40))
                /*transformer.addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = 0.85f + r * 0.14f
                }*/
                binding.viewPager.setPageTransformer(transformer)
                val sectionsRcv: RecyclerView = binding.sectionsRcv
                var sectionsAdapter = SectionsAdapter(context, sections)
                sectionsAdapter.onItemClick = { position, videoPosition ->
                    val intent = Intent(activity, DetailsActivity::class.java)
                    intent.putExtra(Constants.ID, sections[position].video[videoPosition].id)
                    startActivity(intent)
                }
                sectionsAdapter.onViewAllClick = { position ->
                    val intent = Intent(activity, ViewAllActivity::class.java)
                    intent.putExtra(Constants.TITLE, sections[position].title)
                    intent.putExtra(Constants.SECTIONID, sections[position].id)
                    startActivity(intent)
                }
                sectionsRcv.layoutManager = LinearLayoutManager(context)
                sectionsRcv.adapter = sectionsAdapter
                binding.refreshLayout.isRefreshing = false
            }
            override fun onFailure(t: Any?) {
                binding.refreshLayout.isRefreshing = false
            }
            override fun onNetworkFailure() {
                binding.refreshLayout.isRefreshing = false
                progressBar.visibility=View.INVISIBLE
                errorImage.visibility=View.VISIBLE
                loadTitle.text="Network Issue"
                subTitle.text="Please check your internet connection"
                dialog.show()
                yesButton.visibility=View.VISIBLE
                yesButton.text="Retry"
                yesButton.setOnClickListener {
                    dialog.dismiss()
                    getCategory(id)
                }
            }
        },id)
    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable , 2000)
    }

    private val runnable = Runnable {
        binding.viewPager.currentItem =  binding.viewPager.currentItem + 1
    }

}