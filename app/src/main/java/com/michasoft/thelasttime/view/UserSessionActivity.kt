package com.michasoft.thelasttime.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michasoft.thelasttime.LastTimeApplication

/**
 * Created by mśmiech on 03.05.2022.
 */
abstract class UserSessionActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if((application as LastTimeApplication).userSessionComponent == null) {
            LoginActivity.start(this)
            finish()
        }
        onActivityCreate(savedInstanceState)
    }

    abstract fun onActivityCreate(savedInstanceState: Bundle?)
}