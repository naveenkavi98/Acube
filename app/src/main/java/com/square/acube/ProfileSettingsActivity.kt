package com.square.acube

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.square.acube.databinding.ActivityProfileSettingsBinding
import com.square.acube.model.User.Update
import com.square.acube.model.User.profile
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestConstants
import com.square.acube.network.RestController
import java.io.File

class ProfileSettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileSettingsBinding
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var submitButton: Button
    lateinit var textBottomSheet: EditText
    lateinit var textTitle: TextView
    var userName : String =""
    lateinit var userprofile : File
    var userEmail : String =""
    var userID: String = ""
    var country: String?=null
    var mobileId:String?=null
    lateinit var auth: FirebaseAuth
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
        binding= ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val data ="Brand: ${Build.BRAND} \n" +
                "DeviceID: ${
                    Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } \n" +
                "Model: ${Build.MODEL} \n" +
                "ID: ${Build.ID} \n" +
                "SDK: ${Build.VERSION.SDK_INT} \n" +
                "Manufacture: ${Build.MANUFACTURER} \n" +
                "Brand: ${Build.BRAND} \n" +
                "User: ${Build.USER} \n" +
                "Type: ${Build.TYPE} \n" +
                "Base: ${Build.VERSION_CODES.BASE} \n" +
                "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
                "Board: ${Build.BOARD} \n" +
                "Host: ${Build.HOST} \n" +
                "FingerPrint: ${Build.FINGERPRINT} \n" +
                "Version Code: ${Build.VERSION.RELEASE}"*/

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("TOKEN_KEY", Context.MODE_PRIVATE)
        userID = sharedPreferences.getString("TOKEN_KEY", "").toString()
        country=sharedPreferences.getString("COUNTRY","")
        mobileId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        auth = FirebaseAuth.getInstance()


        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(layoutInflater.inflate(R.layout.bottom_sheet_email, null))
        bottomSheetDialog.setCancelable(true)
        submitButton = bottomSheetDialog.findViewById<Button>(R.id.emailSubmitButton)!!
        textBottomSheet=bottomSheetDialog.findViewById<EditText>(R.id.textEmail)!!
        textTitle=bottomSheetDialog.findViewById<TextView>(R.id.title)!!

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

        if(!userID.isNullOrEmpty()) {
            getUser("get user",userID)
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.UserNameLayout.setOnClickListener {
            nameBottomSheet()
        }
        binding.UserEmailLayout.setOnClickListener {
            emailBottomSheet()
        }
        binding.backButton.setOnClickListener { finish() }

        binding.profileContainer.setOnClickListener {
            imageChooser()
        }
        binding.DeleteAccountButton.setOnClickListener {
            subTitle.text="Are you sure you want to delete account?"
            dialog.show()
            yesButton.visibility= View.VISIBLE
            noButton.visibility=View.VISIBLE
            progressBar.visibility=View.INVISIBLE
            errorImage.visibility=View.VISIBLE
            yesButton.setOnClickListener {
                deleteAccount("delete",userID)
            }
            noButton.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    fun logout(){
        val devicename= "${Build.BRAND}+ ${Build.MODEL}"
        createUser("logout",userID, country.toString(),mobileId!!,devicename)
        val sharedPreferences: SharedPreferences =
            this@ProfileSettingsActivity.getSharedPreferences(
                "TOKEN_KEY",
                Context.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("TOKEN_KEY", null)
        editor.putString("COUNTRY", null)
        editor.apply()
        editor.commit()
        dialog.dismiss()
        startActivity(Intent(this@ProfileSettingsActivity, LoginActivity::class.java))
        finish()
        auth.signOut()
    }

    private fun createUser(method:String,phoneno:String,country:String,mobileid:String,devicename:String) {

        val headerModel = HeaderModel()
        RestController(headerModel).createUser(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                Log.e("onResponse","onResponse:$t")
            }

            override fun onFailure(t: Any?) {
                Log.e( "onFailure: ", t.toString())
            }

            override fun onNetworkFailure() {

            }


        },method,phoneno,country,mobileid,devicename)
    }

    fun imageChooser() {

        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
    }

    private fun emailBottomSheet() {
        textTitle.text="E-mail"
        bottomSheetDialog.show()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        textBottomSheet.setText(userEmail)
        submitButton.setOnClickListener {
            if (textBottomSheet.text.matches(emailPattern.toRegex())) {
                userEmail = textBottomSheet.text.toString()
                updateUser("update user",userID,userName,userEmail)
                bottomSheetDialog.dismiss()
            } else {
                textBottomSheet.error = "Invalid email address"
            }
        }

    }

    private fun nameBottomSheet() {
        textTitle.text="Name"
        bottomSheetDialog.show()
        textBottomSheet.setText(userName)
        submitButton.setOnClickListener {
            userName = textBottomSheet.text.toString()
            updateUser("update user",userID,userName,userEmail)
            bottomSheetDialog.dismiss()
        }

    }

    private fun updateUser(method:String,phoneno:String,name:String,email:String) {

        val headerModel = HeaderModel()
        RestController(headerModel).updateUser(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as Update
                val data =response.data
                Log.e("onResponseupdateUser","${t.data}")

                getUser("get user",userID)
            }

            override fun onFailure(t: Any?) {
                Log.e( "onFailure: ", t.toString())
            }

            override fun onNetworkFailure() {

            }


        },method,phoneno, name, email)
    }

    private fun getUser(method:String,phoneno:String) {
        dialog.show()
        val headerModel = HeaderModel()
        RestController(headerModel).getUser(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as profile
                val data =response.data
                Log.e("onResponsegetUser","$t")

                binding.UserPhoneNumber.text= data!!.phoneno
                Glide.with(applicationContext).load(RestConstants.PROFILE_PATH+data.img)
                    .into(binding.profileImage)

                /*if (data.img.isNullOrEmpty()){
                    *//*userName="Enter name"
                    binding.UserName.text= "Enter name"*//*
                }
                else{
                    userprofile=data.img
                    val imageBytes = Base64.decode(userprofile, 0)
                    val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                   // val bitmap: String =userprofile
                    Glide.with(applicationContext).load(image)
                        .into(binding.profileImage)
                }*/

                if (data.name.isNullOrEmpty()){
                    userName="Enter name"
                    binding.UserName.text= "Enter name"
                }
                else{
                    userName= data.name!!
                    binding.UserName.text= userName
                    val sharedPreferences: SharedPreferences =
                        this@ProfileSettingsActivity.getSharedPreferences(
                            "TOKEN_KEY",
                            Context.MODE_PRIVATE
                        )
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("NAME", userName)
                    editor.apply()
                    editor.commit()
                }

                if (data.email.isNullOrEmpty()){
                    userEmail="Enter email"
                    binding.UserEmail.text= "Enter email"
                }
                else{
                    userEmail= data.email!!
                    binding.UserEmail.text= userEmail
                }
                dialog.dismiss()
            }

            override fun onFailure(t: Any?) {
                dialog.dismiss()
                Log.e( "onFailure: ", t.toString())
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
                    getUser("get user",userID)
                }

            }


        },method,phoneno)
    }

    private fun deleteAccount(method:String,phoneno:String){
        val headerModel = HeaderModel()
        RestController(headerModel).deleteAccount(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                Log.e( "deleteAccount: ", t.toString() )
                loadTitle.text="Account Deleted"
                subTitle.text="Account is deleted successfully"
                dialog.show()
                logout()
            }

            override fun onFailure(t: Any?) {

            }

            override fun onNetworkFailure() {
            }

        },method, phoneno)
    }
}