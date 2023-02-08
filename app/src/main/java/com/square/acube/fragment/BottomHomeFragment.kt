package com.square.acube.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.ott.ottapp.adapter.CategoryAdapter
import com.square.acube.CategoryActivity
import com.square.acube.DetailsActivity
import com.square.acube.R
import com.square.acube.ViewAllActivity
import com.square.acube.adapter.HomeRecentlyWatchedAdapter
import com.square.acube.adapter.SectionsAdapter
import com.square.acube.databinding.FragmentBottomHomeBinding
import com.square.acube.model.dashboard.DashboardResponse
import com.square.acube.model.dashboard.Recently
import com.square.acube.model.dashboard.Section
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestConstants
import com.square.acube.network.RestController
import com.square.acube.utils.Constants
import retrofit2.Response.error

class BottomHomeFragment : Fragment() {

    private lateinit var binding: FragmentBottomHomeBinding
    private var userID: String = ""
    private lateinit var dialog: Dialog
    private lateinit var subTitle: TextView
    private lateinit var loadTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorImage: ImageView
    private lateinit var doneImage: ImageView
    private lateinit var yesButton: TextView
    private lateinit var noButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("TOKEN_KEY", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("TOKEN_KEY", "").toString()

        /*imageList.add(SlideModel("https://bit.ly/37Rn50u", ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel("https://bit.ly/2BteuF2", ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel("https://bit.ly/3fLJf72", ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel("https://bit.ly/37Rn50u", ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel("https://bit.ly/2BteuF2", ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel("https://bit.ly/3fLJf72", ScaleTypes.CENTER_CROP))
        binding!!.imageSlider.setImageList(imageList)*/
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.progress_layout)
        subTitle = dialog.findViewById(R.id.subTitle)
        loadTitle = dialog.findViewById(R.id.loadTitle)
        errorImage = dialog.findViewById(R.id.error)
        progressBar = dialog.findViewById(R.id.progress)
        yesButton = dialog.findViewById(R.id.yesButton)
        noButton = dialog.findViewById(R.id.noButton)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.setBackgroundDrawableResource(R.color.trans)
        dialog.window!!.attributes = layoutParams

        binding.refreshLayout.setOnRefreshListener {
            getDashboard(userID)
        }
        getDashboard(userID)
        return root
    }


    private fun getDashboard(phoneno: String) {
        binding.refreshLayout.isRefreshing = true
        val headerModel = HeaderModel()
        RestController(headerModel).getDashboard(context, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as DashboardResponse
                Log.e("getDashboard: ", response.toString())
                val banner = response.banner
                var sections: ArrayList<Section> = ArrayList()
                val imageList = ArrayList<SlideModel>()
                response.banner.forEach {
                    imageList.add(
                        SlideModel(
                            RestConstants.BANNER_PATH + it.image,
                            ScaleTypes.CENTER_CROP
                        )
                    )
                }
                binding.imageSlider.setImageList(imageList)
                binding.imageSlider.setItemClickListener(object : ItemClickListener {
                    override fun onItemSelected(position: Int) {
                        val intent = Intent(activity, DetailsActivity::class.java)
                        intent.putExtra(Constants.ID, banner[position].videoid)
                        startActivity(intent)
                    }
                })
                sections.addAll(response.section)
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

                val categoryRcv: RecyclerView = binding.categoryRcv
                var categoryAdapter = CategoryAdapter(context, response.categorytop)
                categoryAdapter.onItemClick = { position ->
                    val intent = Intent(activity, CategoryActivity::class.java)
                    intent.putExtra(Constants.ID, response.categorytop[position].id)
                    startActivity(intent)
                }
                categoryRcv.adapter = categoryAdapter
                val recent: ArrayList<Recently> = response.recently
                if (recent.isNotEmpty()) {
                    var favouriteAdapter = HomeRecentlyWatchedAdapter(context!!, recent)
                    favouriteAdapter.onItemClick = { videoPosition ->
                        val intent = Intent(requireContext(), DetailsActivity::class.java)
                        intent.putExtra(Constants.ID, recent[videoPosition].id)
                        startActivity(intent)
                    }
                    binding.videosRcv.adapter = favouriteAdapter
                } else {
                    binding.textRecent.visibility = View.GONE
                    binding.videosRcv.visibility = View.GONE
                }
                val sharedPreferences: SharedPreferences =
                    requireContext().getSharedPreferences("TOKEN_KEY", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("RazKey", response.RazKey)
                editor.apply()
                editor.commit()
                binding.refreshLayout.isRefreshing = false
            }
            override fun onFailure(t: Any?) {
                binding.refreshLayout.isRefreshing = false
            }
            override fun onNetworkFailure() {
                binding.refreshLayout.isRefreshing = false
                progressBar.visibility = View.INVISIBLE
                errorImage.visibility = View.VISIBLE
                loadTitle.text = "Network Issue"
                subTitle.text = "Please check your internet connection"
                dialog.show()
                yesButton.visibility = View.VISIBLE
                yesButton.text = "Retry"
                yesButton.setOnClickListener {
                    dialog.dismiss()
                    getDashboard(phoneno)
                }
            }
        }, phoneno)
    }

}