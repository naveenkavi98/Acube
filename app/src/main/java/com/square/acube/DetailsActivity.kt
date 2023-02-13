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
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.square.acube.adapter.RelatedAdapter
import com.square.acube.databinding.ActivityDetailsBinding
import com.square.acube.model.details.VideoDetailsResponse
import com.square.acube.model.favourite.AddFavourite
import com.square.acube.model.recentlywatched.AddRecent
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestController
import com.square.acube.utils.CommonUtils.Companion.getImageUrl
import com.square.acube.utils.Constants

class DetailsActivity : AppCompatActivity() {

    private var ID: String? = ""
    private var SEARCH: String? = ""
    private var URL: String? = null
    private var userID: String = ""
    private lateinit var dialog: Dialog
    private lateinit var subTitle: TextView
    private lateinit var loadTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorImage: ImageView
    private lateinit var yesButton: TextView
    private lateinit var noButton: TextView
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("TOKEN_KEY", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("TOKEN_KEY", "").toString()

        ID = intent.getStringExtra(Constants.ID)
        SEARCH = intent.getStringExtra(Constants.SEARCH)

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
        binding.refreshLayout.setOnRefreshListener { getDetails(SEARCH.toString()) }
        getDetails(SEARCH.toString())
    }

    private fun getDetails(SEARCH: String) {
        binding.refreshLayout.isRefreshing = true
        val headerModel = HeaderModel()
        ID?.let {
            RestController(headerModel).getVideoDetails(
                this,
                userID,
                it,
                object : ResponseCallback {
                    override fun onResponse(t: Any?) {
                        binding.refreshLayout.isRefreshing = false
                        val response = t as VideoDetailsResponse
                        Log.e("onResponse: ", response.toString())
                        //binding.title.text = response.video?.title
                        binding.plotLabel.text = response.video?.title
                        binding.plotValue.text = response.video?.shortdescription
                        binding.directorValue.text = response.video?.direacter
                        binding.producedValue.text = response.video?.producer
                        binding.castValue.text = response.video?.cast
                        binding.musicDirecterValue.text = response.video?.musicdireacter
                        binding.dateOfReleaseValue.text = response.video?.dor!!.split(" ")[0]
                        if (!response.video?.certificate.isNullOrEmpty()) {
                            binding.certificationValue.text = response.video?.certificate
                        } /*else {
                            binding.certificate.visibility = View.GONE
                        }*/
                        if (!response.language!!.languageName.isNullOrEmpty()) {
                            binding.languageValue.text = response.language!!.languageName
                        } else {
                            binding.languageValue.visibility = View.GONE
                        }
                        if (!response.gener?.categoryname.isNullOrEmpty()) {
                            binding.genre.text = response.gener?.categoryname
                        } else {
                            binding.genre.visibility = View.GONE
                        }
                        /*if (!response.video?.age.isNullOrEmpty()) {
                            binding.age.text = response.video?.age
                        } else {
                            binding.age.visibility = View.GONE
                        }*/
                        /*if (!response.video?.imdb.isNullOrEmpty()) {
                            binding.imdb.text = response.video?.imdb
                        } else {
                            binding.imdb.visibility = View.GONE
                        }*/
                        Glide.with(this@DetailsActivity).load(getImageUrl(response.video?.image2))
                            .into(binding.mainImage)
                        if (response.video?.relesingsoon == "0") {
                            URL = response.video?.embed_url
                        } else {
                            binding.play.visibility = View.GONE
                            binding.textComingSoon.visibility = View.VISIBLE
                        }

                        /*if (response.iswishlist == true) {
                            binding.removeFavoriteButton.visibility = View.VISIBLE
                            binding.addFavoriteButton.visibility = View.GONE
                        } else {
                            binding.removeFavoriteButton.visibility = View.GONE
                            binding.addFavoriteButton.visibility = View.VISIBLE
                        }
*/
                        val relatedAdapter =
                            RelatedAdapter(applicationContext, response.relatedvideo)
                        relatedAdapter.onItemClick = { Position ->
                            val intent = Intent(this@DetailsActivity, DetailsActivity::class.java)
                            intent.putExtra(Constants.ID, response.relatedvideo[Position].id)
                            startActivity(intent)
                        }
                        binding.videosRcv.adapter = relatedAdapter

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
                            getDetails(SEARCH)
                        }
                    }
                },
                SEARCH
            )
        }
    }

    private fun initView() {
        binding.backButton.setOnClickListener { finish() }
        binding.play.setOnClickListener {

            if (!URL.isNullOrEmpty()) {
                addRecentlyWatched("add", "$ID", userID)
                val intent = Intent(this@DetailsActivity, VideoPlayerActivity::class.java)
                intent.putExtra(Constants.URL, URL)
                startActivity(intent)
                /*if (URL?.contains("vimeo.com") == false) {
                    URL?.let {
                        addRecentlyWatched("add", "$ID", userID)
                        val intent = Intent(this, PlayerActivity::class.java)
                        intent.putExtra(Constants.URL, URL)
                        startActivity(intent)
                    }
                } else {
//                    binding.refreshLayout.isRefreshing = true
                    VimeoExtractor.getInstance().fetchVideoWithURL(
                        URL!!,
                        null,
                        object : OnVimeoExtractionListener {
                            override fun onSuccess(video: VimeoVideo) {
//                                binding.refreshLayout.isRefreshing = false
                                val (key) = video.streams.iterator().next()
                                val hdStream = video.streams[key]
                                hdStream?.let {
                                    addRecentlyWatched("add", "$ID", userID)
                                    val intent =
                                        Intent(this@DetailsActivity, PlayerActivity::class.java)
                                    intent.putExtra(Constants.URL, hdStream)
                                    intent.putExtra(Constants.STREAMS, video.streams as (Serializable))
                                    startActivity(intent)
                                }
                            }
                            override fun onFailure(throwable: Throwable) {
                                //Error handling here
//                                binding.refreshLayout.isRefreshing = false
                                Log.e("TAG", "onFailure: ")
                            }
                        })
                }*/
//                val browserIntent = Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("http://player.vimeo.com/video/716319547")
//                )
//                browserIntent.setPackage("com.vimeo.android.videoapp")
//               try {
//                   startActivity(browserIntent)
//               } catch (e:Exception){
//                   val i = Intent(Intent.ACTION_VIEW)
//                   i.data = Uri.parse(url)
//                   startActivity(i)
//               }
            } else {
                val snack =
                    Snackbar.make(binding.root, "Upgrade your plan to play this video", Snackbar.LENGTH_LONG)
                snack.setAction("Ok") {
                    startActivity(Intent(applicationContext, SubscriptionPlansActivity::class.java))
                    finish()
                }
                snack.show()
            }
        }
        binding.share.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.ott.ottapp&hl=en&gl=US")
            startActivity(Intent.createChooser(i, "Share URL"))
        }
        /*binding.addFavoriteButton.setOnClickListener {
            if(!userID.isNullOrEmpty()) {
                addFavourite("add", "$ID", userID)
                val snack = Snackbar.make(it, "Add to favourite", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else{
                val snack =
                    Snackbar.make(binding.root, "Login to continue!", Snackbar.LENGTH_LONG)
                snack.setAction("Ok") {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                snack.show()

            }
        }*/
        /*binding.removeFavoriteButton.setOnClickListener {
            if(!userID.isNullOrEmpty()) {
                addFavourite("remove", "$ID", userID)
                val snack = Snackbar.make(it, "Remove from favourite", Snackbar.LENGTH_LONG)
                snack.show()
            }
            else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }*/
    }

    private fun addFavourite(method: String, videoid: String, phoneno: String) {
        /*val progressDialog =SweetAlertDialog(this@DetailsActivity, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog.setTitleText("Loading")
        progressDialog.show()*/
        val headerModel = HeaderModel()
        RestController(headerModel).addFavourite(this, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as AddFavourite
                Log.e("onResponsegetMyFalljlvourite", "$response")
                getDetails(SEARCH.toString())
                //progressDialog.dismiss()
            }

            override fun onFailure(t: Any?) {
                Log.e("onFailure: ", t.toString())
            }

            override fun onNetworkFailure() {

            }


        }, method, videoid, phoneno)

    }

    private fun addRecentlyWatched(method: String, videoid: String, phoneno: String) {

        val headerModel = HeaderModel()
        RestController(headerModel).addRecentlyWatched(this, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as AddRecent
                val data = response.data
                Log.e("onResponsegetRecentlyWatched", "$response")


            }

            override fun onFailure(t: Any?) {
                Log.e("onFailure: ", t.toString())
            }

            override fun onNetworkFailure() {

            }


        }, method, videoid, phoneno)
    }

}