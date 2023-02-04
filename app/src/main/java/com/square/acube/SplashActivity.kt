package com.square.acube

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        navigate()
    }

    private fun navigate() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)
    }
}