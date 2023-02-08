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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.square.acube.CategoryActivity
import com.square.acube.R
import com.square.acube.adapter.MoreAdapter
import com.square.acube.databinding.FragmentBottomMoreBinding
import com.square.acube.model.dashboard.DashboardResponse
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestController
import com.square.acube.utils.Constants


class BottomMoreFragment : Fragment() {

    private lateinit var binding: FragmentBottomMoreBinding
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
        binding = FragmentBottomMoreBinding.inflate(inflater, container, false)

        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("TOKEN_KEY", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("TOKEN_KEY", "").toString()

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

        binding.refreshLayout.setOnRefreshListener { getDashboard(userID) }
        getDashboard(userID)
        return binding.root
    }

    private fun getDashboard(phoneno: String) {
        binding.refreshLayout.isRefreshing = true
        val headerModel = HeaderModel()
        RestController(headerModel).getDashboard(context, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as DashboardResponse
                Log.e("onResponsejgjhbj: ", response.toString())

                val topSeriesRcv: RecyclerView = binding.topSeriesRcv
                var moreAdapter = MoreAdapter(context, response.categorytop)
                moreAdapter.onItemClick = { position ->
                    val intent = Intent(activity, CategoryActivity::class.java)
                    intent.putExtra(Constants.ID, response.categorytop[position].id)
                    startActivity(intent)
                }
                topSeriesRcv.layoutManager = GridLayoutManager(context,2)
                topSeriesRcv.adapter = moreAdapter
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
                    getDashboard(phoneno)
                }
            }
        },phoneno)
    }

}