package com.square.acube

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.square.acube.databinding.ActivityMainBinding
import com.square.acube.fragment.BottomAlbumFragment
import com.square.acube.fragment.BottomHomeFragment
import com.square.acube.fragment.BottomMoreFragment
import com.square.acube.fragment.BottomMoviesFragment
import com.square.acube.model.User.CheckUsers
import com.square.acube.model.User.Session
import com.square.acube.model.User.profile
import com.square.acube.model.forceupdate.ForceUpdateResponse
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestController
import com.square.acube.utils.Constants


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    var userID: String?=null
    var userName: String?=null
    var country: String?=null
    var mobileId:String?=null
    var login:String?=null
    lateinit var handler: Handler
    lateinit var auth: FirebaseAuth
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var title: TextView
    lateinit var yesButton: Button
    lateinit var noButton:Button
    lateinit var navLnrUser: LinearLayout
    lateinit var navLnrLogin: LinearLayout
    lateinit var navUserName: TextView
    lateinit var navUserNumber: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        forceUpdate(BuildConfig.VERSION_NAME)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("TOKEN_KEY", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("TOKEN_KEY", "").toString()
        country=sharedPreferences.getString("COUNTRY", "").toString()
        userName=sharedPreferences.getString("NAME", "").toString()
        mobileId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        bottomSheetDialog = BottomSheetDialog(this@MainActivity)
        bottomSheetDialog.setContentView(layoutInflater.inflate(R.layout.bottom_sheet_login, null))
        bottomSheetDialog.setCancelable(false)
        title= bottomSheetDialog.findViewById<TextView>(R.id.title)!!
        yesButton=bottomSheetDialog.findViewById<Button>(R.id.yesButton)!!
        noButton=bottomSheetDialog.findViewById<Button>(R.id.noButton)!!
        login=intent.getStringExtra(Constants.LOGIN)
        handler= Handler(Looper.getMainLooper())
        val hView = binding.navView.getHeaderView(0)
        navLnrLogin = hView.findViewById<LinearLayout>(R.id.lnr_nav_login)
        navLnrUser = hView.findViewById<LinearLayout>(R.id.lnr_nav_user)
        navUserName = hView.findViewById<TextView>(R.id.txt_nav_user_name)
        navUserNumber = hView.findViewById<TextView>(R.id.txt_nav_mobile_number)
        val menu = binding.navView.menu
        menu.findItem(R.id.navigation_version).title = "Version "+BuildConfig.VERSION_NAME
        navLnrLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        navLnrUser.setOnClickListener {
            val intent = Intent(this, ProfileSettingsActivity::class.java)
            startActivity(intent)
        }
        if(!userID.isNullOrEmpty()) {
            navLnrLogin.visibility= View.GONE
            navLnrUser.visibility= View.VISIBLE
            navUserName.text = userName.toString()
            navUserNumber.text = userID.toString()
            menu.findItem(R.id.navigation_recently_watched).isVisible = true
            menu.findItem(R.id.navigation_watch_later).isVisible = true
            menu.findItem(R.id.navigation_membership).isVisible = false
            //getUser("get user", userID!!)
        }
        else {
            navLnrLogin.visibility= View.VISIBLE
            navLnrUser.visibility= View.GONE
            menu.findItem(R.id.navigation_recently_watched).isVisible = false
            menu.findItem(R.id.navigation_watch_later).isVisible = false
            menu.findItem(R.id.navigation_membership).isVisible = false
        }
        /*GlobalScope.launch {
            while (isActive) {
                loginOutStatus(mobileId!!, "loginoutstatus", userID)
                Log.e("TAG", "onCreate: ",)
                delay(5000)
            }
        }*/
        toggle = ActionBarDrawerToggle(this@MainActivity, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragment(BottomHomeFragment())
        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(BottomHomeFragment())
                R.id.nav_movies -> replaceFragment(BottomMoviesFragment())
                R.id.nav_clips -> replaceFragment(BottomAlbumFragment())
                R.id.nav_more -> replaceFragment(BottomMoreFragment())
            }
            true
        }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_recently_watched -> {
                    val intent = Intent(this, RecentlyWatchedActivity::class.java)
                    startActivity(intent)
                }
                R.id.navigation_watch_later -> {
                    val intent = Intent(this, MyFavortiesActivity::class.java)
                    startActivity(intent)
                }
                R.id.navigation_membership -> {
                    val intent = Intent(this, SubscriptionPlansActivity::class.java)
                    startActivity(intent)
                }
                R.id.navigation_help_and_support -> {
                    //startIntent("https://starupmediaworld.com/contact.html")
                }
                R.id.navigation_terms -> {
                    //startIntent("https://starupmediaworld.com/terms.html")
                }
                R.id.navigation_privacy_and_policy -> {
                    //startIntent("https://starupmediaworld.com/privacy.html")
                }
                R.id.navigation_disclaimer -> {
                    //startIntent("https://starupmediaworld.com/disclamer.html")
                }
                R.id.navigation_refund_and_cancellation -> {
                    //startIntent("https://starupmediaworld.com/cancel.html")
                }
                R.id.navigation_shipping_and_delivery -> {
                    //startIntent("https://starupmediaworld.com/shipping.html")
                }
            }
            true
        }
        binding.menu.setOnClickListener {
            binding.drawerLayout.open()
        }
        binding.searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startIntent(url:String) {
        val viewIntent = Intent(
            "android.intent.action.VIEW",
            Uri.parse(url)
        )
        startActivity(viewIntent)
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_activity_dashboard,fragment)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun checkUser(mobileid:String,method:String,phoneno:String) {
        val headerModel = HeaderModel()
        RestController(headerModel).checkUser(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                val response= t as CheckUsers
                Log.e("CheckUsers","onResponse:$t")
                if (!response.lessUser!!){
                    noButton.visibility= View.VISIBLE
                    title!!.text="Click yes to logout from other devices...!!!"
                    bottomSheetDialog.show()
                    yesButton!!.setOnClickListener {
                        logoutUser(mobileid,"checkuser",userID!!,"logoutlast")
                        bottomSheetDialog.dismiss()
                    }
                    noButton!!.setOnClickListener {
                        logout()
                        bottomSheetDialog.dismiss()
                    }
                }
            }

            override fun onFailure(t: Any?) {
                Log.e( "onFailure: ", t.toString())
            }
            override fun onNetworkFailure() {
            }


        },mobileid,method,phoneno)
    }

    fun logout(){
        val devicename= "${Build.BRAND}+ ${Build.MODEL}"
        createUser("logout",userID!!, country.toString(),mobileId!!,devicename)
        val sharedPreferences: SharedPreferences =
            this@MainActivity.getSharedPreferences(
                "TOKEN_KEY",
                Context.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("TOKEN_KEY", null)
        editor.putString("COUNTRY", null)
        editor.apply()
        editor.commit()
        auth.signOut()
        finish()
        startActivity(intent)
    }

    private fun logoutUser(mobileid:String,method:String,phoneno:String,logoutlast:String) {
        val headerModel = HeaderModel()
        RestController(headerModel).logoutUser(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                Log.e("logoutUser","onResponse:$t")
            }
            override fun onFailure(t: Any?) {
                Log.e( "onFailure: ", t.toString())
            }
            override fun onNetworkFailure() {
            }
        },mobileid,method,phoneno,logoutlast)
    }

    private fun loginOutStatus(mobileid:String,method:String,phoneno:String) {
        val headerModel = HeaderModel()
        RestController(headerModel).loginOutStatus(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                val response= t as Session
                Log.e("loginOutStatus","$t")
                if (!response.status!!){
                    title!!.text="You logout from other...!!!"
                    bottomSheetDialog.show()
                    yesButton!!.setOnClickListener {
                        finish();
                        startActivity(intent);
                        bottomSheetDialog.dismiss()
                    }
                    noButton.visibility=View.GONE
                    /*noButton!!.setOnClickListener {
                        startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                        finish()
                        bottomSheetDialog.dismiss()
                    }*/
                    val devicename= "${Build.BRAND}+ ${Build.MODEL}"
                    createUser("logout",userID!!, country.toString(),mobileId!!,devicename)
                    val sharedPreferences: SharedPreferences =
                        this@MainActivity.getSharedPreferences(
                            "TOKEN_KEY",
                            Context.MODE_PRIVATE
                        )
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("TOKEN_KEY", null)
                    editor.putString("COUNTRY", null)
                    editor.apply()
                    editor.commit()
                    auth.signOut()
                    Log.e( "loginoutstatusjgjh: ","hj" )
                }
            }
            override fun onFailure(t: Any?) {
                Log.e( "onFailure: ", t.toString())
            }
            override fun onNetworkFailure() {
            }
        },mobileid,method,phoneno)
    }

    private fun createUser(method:String,phoneno:String,country:String,mobileid:String,devicename:String) {

        val headerModel = HeaderModel()
        RestController(headerModel).createUser(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                Log.e("createUser","onResponse:$t")
            }

            override fun onFailure(t: Any?) {
                Log.e( "onFailure: ", t.toString())
            }

            override fun onNetworkFailure() {

            }


        },method,phoneno,country,mobileid,devicename)
    }

    private fun forceUpdate(text: String) {
        val headerModel = HeaderModel()
        RestController(headerModel).forceUpdate(this, "1.1.1", object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as ForceUpdateResponse
                Log.e("onResponseForceUpdate: ", response.toString())
                if (response.status!!) {
                    title.text="Please update the app !!!"
                    if (response.force_update!!){
                        yesButton.text="Update"
                        noButton.visibility=View.INVISIBLE
                        yesButton.setOnClickListener {
                            val viewIntent = Intent(
                                "android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/details?id=com.ott.ottapp")
                            )
                            startActivity(viewIntent)
                        }
                    }
                    else{
                        yesButton.setOnClickListener {
                            val viewIntent = Intent(
                                "android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/details?id=com.ott.ottapp")
                            )
                            startActivity(viewIntent)
                        }
                        noButton.setOnClickListener {
                            bottomSheetDialog.dismiss()
                        }
                    }
                    bottomSheetDialog.show()
                }
            }

            override fun onFailure(t: Any?) {
            }

            override fun onNetworkFailure() {
            }
        })
    }
    override fun onStart() {
        super.onStart()
        if (!userID.isNullOrEmpty()){
            if (!login.isNullOrEmpty()){
                checkUser(mobileId!!,"checkuser",userID!!)
            }
            handler.post(object : Runnable {
                override fun run() {
                    loginOutStatus(mobileId!!, "loginoutstatus", userID!!)
                    handler.postDelayed(this, 5000)
                }
            })
        }

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    private fun getUser(method:String,phoneno:String) {

        val headerModel = HeaderModel()
        RestController(headerModel).getUser(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as profile
                val data =response.data!!
                navUserName.text = data.name
                navUserNumber.text = data.phoneno
                /*Glide.with(applicationContext).load(RestConstants.PROFILE_PATH+ data!!.img)
                    .into(binding.ProfileImage)*/
            }
            override fun onFailure(t: Any?) {
                Log.e( "onFailure: ", t.toString())
            }
            override fun onNetworkFailure() {
            }
        },method,phoneno)
    }
}