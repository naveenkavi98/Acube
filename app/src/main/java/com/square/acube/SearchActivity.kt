package com.square.acube

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.square.acube.adapter.RecentSearchAdapter
import com.square.acube.adapter.SearchAdapter
import com.square.acube.databinding.ActivitySearchBinding
import com.square.acube.model.search.RecentSearchData
import com.square.acube.model.search.RecentSearchResponse
import com.square.acube.model.search.SearchResponse
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestController
import com.square.acube.utils.Constants

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var sectionsRcv: RecyclerView
    private var userID: String = ""
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
        binding = ActivitySearchBinding.inflate(layoutInflater)
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

        initView()
    }


    private fun initView() {
        sectionsRcv = binding.sectionsRcv
        sectionsRcv.layoutManager = GridLayoutManager(this, 3)
        binding.backButton.setOnClickListener { finish() }
        binding.searchEdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search(binding.searchEdt.text.toString())
//                if (binding.searchEdt.text?.length == 6) {
////                    navigate()
//                    if (view != null) {
//                        val imm: InputMethodManager =
//                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                        imm.hideSoftInputFromWindow(otpComponent.getWindowToken(), 0)
//                    }
//                }
            }
        })
        recentSearch(userID)
        search("")
    }

    private fun search(text: String) {
        val headerModel = HeaderModel()
        RestController(headerModel).search(this, text, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as SearchResponse
                Log.e("\n\n\n\n\nonResponse: ", response.toString())
                val sections=response.data

                val sectionsRcv: RecyclerView = binding.sectionsRcv
                binding.sectionsRcv.isNestedScrollingEnabled = false
                var sectionsAdapter = SearchAdapter(this@SearchActivity, sections)
                sectionsAdapter.onItemClick = { position, videoPosition ->
                    val intent = Intent(this@SearchActivity, DetailsActivity::class.java)
                    intent.putExtra(Constants.ID, sections[position].video[videoPosition].id)
                    intent.putExtra(Constants.SEARCH, "true")
                    startActivity(intent)
                }
                sectionsAdapter.onViewAllClick = { position ->
                    val intent = Intent(this@SearchActivity, SearchViewAllActivity::class.java)
                    intent.putExtra(Constants.TITLE, sections[position].title)
                    intent.putExtra(Constants.SECTIONID, sections[position].id)
                    startActivity(intent)
                }
                sectionsRcv.layoutManager = LinearLayoutManager(this@SearchActivity)
                sectionsRcv.adapter = sectionsAdapter
            }

            override fun onFailure(t: Any?) {
            }

            override fun onNetworkFailure() {
                progressBar.visibility= View.INVISIBLE
                errorImage.visibility= View.VISIBLE
                loadTitle.text="Network Issue"
                subTitle.text="Please check your internet connection"
                dialog.show()
                yesButton.visibility= View.VISIBLE
                yesButton.text="Retry"
                yesButton.setOnClickListener {
                    dialog.dismiss()
                    search(text)
                }
            }
        })
    }

    private fun recentSearch(text: String) {
        val headerModel = HeaderModel()
        RestController(headerModel).recentSearch(this, text, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as RecentSearchResponse
                Log.e("\n\n\n\n\nonResponse: ", response.toString())
                val recentSearchData=response.data

                if (recentSearchData.isNotEmpty()){
                    var sectionsAdapter = RecentSearchAdapter(this@SearchActivity,
                        recentSearchData as ArrayList<RecentSearchData>
                    )
                    sectionsAdapter.onItemClick = { position->
                        val intent = Intent(this@SearchActivity, DetailsActivity::class.java)
                        intent.putExtra(Constants.ID, recentSearchData[position].id)
                        intent.putExtra(Constants.SEARCH, "true")
                        startActivity(intent)
                    }

                    binding.recentRcv.layoutManager = LinearLayoutManager(this@SearchActivity)
                    binding.recentRcv.adapter = sectionsAdapter
                }
                else{
                    binding.recentRcv.visibility=View.GONE
                }

            }

            override fun onFailure(t: Any?) {
            }

            override fun onNetworkFailure() {
            }
        })
    }
}