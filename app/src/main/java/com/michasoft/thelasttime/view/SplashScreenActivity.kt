package com.michasoft.thelasttime.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.michasoft.thelasttime.MainActivity
import com.michasoft.thelasttime.R
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            val splashScreenMinDuration = async { delay(SPLASH_SCREEN_DURATION_IN_MILLIS) }
            val intent = if (Firebase.auth.currentUser != null) {
                Intent(this@SplashScreenActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashScreenActivity, LoginActivity::class.java)
            }
            splashScreenMinDuration.await()
            startActivity(intent)
        }
    }

    companion object {
        const val SPLASH_SCREEN_DURATION_IN_MILLIS = 1000L
    }
}