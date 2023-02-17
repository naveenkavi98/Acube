package com.square.acube

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.hbb20.CountryCodePicker
import com.square.acube.databinding.ActivityLoginBinding
import com.square.acube.model.User.CheckAccount
import com.square.acube.model.User.New
import com.square.acube.network.HeaderModel
import com.square.acube.network.ResponseCallback
import com.square.acube.network.RestController
import com.square.acube.utils.Constants
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var timer: CountDownTimer
    private lateinit var dialog: Dialog
    private lateinit var subTitle: TextView
    private lateinit var loadTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorImage: ImageView
    private lateinit var yesButton: TextView
    private lateinit var noButton: TextView
    val TAG = "LoginActivity"
    var number: String = ""
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var country: String? = null
    var smsCode: String? = null
    var mobileId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        mobileId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        //requestPermissions()
        dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.progress_layout)
        subTitle=dialog.findViewById(R.id.subTitle)
        loadTitle=dialog.findViewById(R.id.loadTitle)
        errorImage=dialog.findViewById(R.id.error)
        progressBar=dialog.findViewById(R.id.progress)
        yesButton=dialog.findViewById(R.id.yesButton)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height=WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.setBackgroundDrawableResource(R.color.trans)
        dialog.window!!.attributes= layoutParams

        val resendOtp = binding.reSendOtp
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resendOtp.text = "seconds remaining: " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                resendOtp.text = "Resend OTP"
            }
        }
        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                /*signInWithPhoneAuthCredential(credential)
                startActivity(Intent(applicationContext, DashboardActivity::class.java))
                finish()*/
                val otpView = binding.otpView
                smsCode = credential.smsCode
                otpView.setText(smsCode)
                val verify = binding.textVerified
                val otpSentView = binding.otpSentView
                verify.visibility = View.VISIBLE
                otpSentView!!.visibility = View.INVISIBLE
                Log.d(TAG, "onVerificationCompleted Success")
                dialog.dismiss()
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                dialog.dismiss()
                Log.d(TAG, "onVerificationFailed  $e")
            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                dialog.dismiss()
                showBottomSheet()
            }
        }
        binding.loginButton.setOnClickListener {
            val phone = binding.phone.text
            val ccp: CountryCodePicker = binding.ccp
            val code = ccp.selectedCountryCode
            dialog.show()
            checkUserAccount("checknumber","$code$phone")
        }
    }

    private fun login() {
        val phone = binding.phone.text
        val ccp: CountryCodePicker = binding.ccp
        val code = ccp.selectedCountryCode
        country = binding.ccp.selectedCountryName
        number = "+$code$phone"
        // get the phone number from edit text and append the country cde with it
        if (binding.phone.text.length == 10) {
            changeAlertType(false)
            subTitle.text="Verifying Phone Number..."
            dialog.show()
            sendVerificationCode(number)
        } else {
            changeAlertType(true)
            subTitle.text="Enter  Valid mobile number"
            dialog.show()
            Handler().postDelayed(Runnable {
                dialog.dismiss()
            }, 2000)
        }
    }

    private fun sendVerificationCode(number: String) {
        val otpView = binding.otpView
        smsCode = null
        otpView.text = smsCode
        val verify = binding.textVerified
        val otpSentView = binding.otpSentView
        verify.visibility = View.INVISIBLE
        otpSentView.visibility = View.VISIBLE
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d(TAG, "Auth started")
    }

    private fun reSendVerificationCode(
        number: String,
        resendToken: PhoneAuthProvider.ForceResendingToken
    ) {
        changeAlertType(false)
        subTitle.text = "Resending Code..."
        dialog.show()
        val otpView = binding.otpView
        smsCode = null
        otpView.text = smsCode
        val verify = binding.textVerified
        val otpSentView = binding.otpSentView
        verify.visibility = View.INVISIBLE
        otpSentView.visibility = View.VISIBLE
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d(TAG, "Auth started")
    }

    private fun verifyPhoneNumberWithCode(storedVerificationId: String, otp: String) {
        subTitle.text = "Verifying!"
        dialog.show()
        val credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential(storedVerificationId, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun showBottomSheet() {
        timer.cancel()
        binding.consLogin.visibility = View.GONE
        binding.consVerify.visibility= View.VISIBLE
        val resendOtp = binding.reSendOtp
        timer.start()
        val submitButton = binding.submitButton
        val otpComponent = binding.otpView
        submitButton!!.setOnClickListener {
            if (otpComponent!!.text.toString().isNotEmpty()) {
                verifyPhoneNumberWithCode(storedVerificationId, otpComponent.text.toString())
                //bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
        resendOtp.setOnClickListener {
            //bottomSheetDialog.dismiss()
            timer.cancel()
            reSendVerificationCode(number, resendToken)
        }
        otpComponent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (otpComponent.text?.length == 6) {
//                    navigate()
                    /*if (bottomSheetDialog != null) {
                        val imm: InputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(otpComponent.windowToken, 0)

                    }*/
                }
            }
        })
        val otpSentView = binding.otpSentView
        otpSentView!!.text = "OTP Sent to " + binding.phone.text
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //bottomSheetDialog.dismiss()
                    dialog.dismiss()
                    changeAlertType(false)
                    subTitle.text = "Welcome to ACube"
                    dialog.show()
                    number = auth.currentUser!!.phoneNumber!!.replace("+", "")
                    Log.e(TAG, "signInWithPhoneAuthCredential: ${auth.currentUser!!.uid}")
                    val devicename = "${Build.BRAND}+ ${Build.MODEL}"
                    createUser("new user", number, country.toString(), mobileId!!, devicename)
                } else {
                    changeAlertType(true)
                    subTitle.text = "wrong OTP"
                    dialog.show()
                    Handler().postDelayed(Runnable {
                        dialog.dismiss()
                    }, 2000)
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        //Toast.makeText(this,"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun changeAlertType(errorType: Boolean) {
        if (errorType) {
            progressBar.visibility = View.INVISIBLE
            errorImage.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.VISIBLE
            errorImage.visibility = View.INVISIBLE
        }
    }

    private fun createUser(
        method: String,
        phoneno: String,
        country: String,
        mobileid: String,
        devicename: String
    ) {
        val headerModel = HeaderModel()
        RestController(headerModel).createUser(this, object : ResponseCallback {
            override fun onResponse(t: Any?) {
                val response = t as New
                Log.e("createUser", "$t")
                //binding.progressDialog.visibility = View.GONE
                /*if (!response.lessUser){
                   *//* changeAlertType(SweetAlertDialog.WARNING_TYPE)
                    binding.subTitle.text="Are you sure logout from other  device"
                    progressDialog.confirmText = "Yes,do it!"
                    progressDialog.setConfirmClickListener { sDialog ->
                        logoutUser("new user",number, country.toString(), mobileId!!,devicename,"logoutlast")
                        val sharedPreferences: SharedPreferences =
                            this@LoginActivity.getSharedPreferences(
                                "TOKEN_KEY",
                                Context.MODE_PRIVATE
                            )
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("TOKEN_KEY", number)
                        editor.apply()
                        editor.commit()
                        Handler().postDelayed(Runnable {
                            sDialog.dismissWithAnimation()
                            val intent = Intent(applicationContext , DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 2000)
                    }
                    binding.progressDialog.visibility = View.VISIBLE*//*
                    val snack =
                        Snackbar.make(binding.root, "Login to continue!", Snackbar.LENGTH_INDEFINITE)
                    snack.setAction("Ok") {
                        logoutUser("new user",number, country.toString(), mobileId!!,devicename,"logoutlast")
                        val sharedPreferences: SharedPreferences =
                            this@LoginActivity.getSharedPreferences(
                                "TOKEN_KEY",
                                Context.MODE_PRIVATE
                            )
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("TOKEN_KEY", number)
                        editor.apply()
                        editor.commit()
                        Handler().postDelayed(Runnable {
                            val intent = Intent(applicationContext , DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 2000)
                    }
                    snack.show()
                }
                else{
                    val sharedPreferences: SharedPreferences =
                        this@LoginActivity.getSharedPreferences(
                            "TOKEN_KEY",
                            Context.MODE_PRIVATE
                        )
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("TOKEN_KEY", number)
                    editor.apply()
                    editor.commit()
                    Handler().postDelayed(Runnable {
                        binding.progressDialog.visibility = View.GONE
                        val intent = Intent(applicationContext , DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 2000)
                }*/
                val sharedPreferences: SharedPreferences =
                    this@LoginActivity.getSharedPreferences(
                        "TOKEN_KEY",
                        Context.MODE_PRIVATE
                    )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("TOKEN_KEY", number)
                editor.putString("COUNTRY", country)
                editor.apply()
                editor.commit()
                Handler().postDelayed(Runnable {
                    dialog.dismiss()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra(Constants.LOGIN, "1")
                    startActivity(intent)
                    finish()
                }, 2000)

            }

            override fun onFailure(t: Any?) {
                Log.e("onFailure: ", t.toString())
            }

            override fun onNetworkFailure() {

            }


        }, method, phoneno, country, mobileid, devicename)
    }

    private fun checkUserAccount(method:String,phoneno:String){
        val headerModel = HeaderModel()
        RestController(headerModel).checkUserAccount(this,object: ResponseCallback {
            override fun onResponse(t: Any?) {
                Log.e( "checkUserAccount: ", t.toString())
                val response = t as CheckAccount
                if (response.status == true) {
                    login()
                }
                else {
                    progressBar.visibility= View.INVISIBLE
                    errorImage.visibility= View.VISIBLE
                    loadTitle.text="Account Deactivated"
                    subTitle.text="Account is deactivate contact Admin"
                    dialog.show()
                    yesButton.visibility= View.VISIBLE
                    yesButton.text="close"
                    yesButton.setOnClickListener {
                        dialog.dismiss()
                        finish()
                    }
                }
            }

            override fun onFailure(t: Any?) {

            }

            override fun onNetworkFailure() {
            }

        },method, phoneno)
    }

}