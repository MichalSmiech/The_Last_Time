package com.michasoft.thelasttime.eventLabels

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EventLabelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        private const val ARG_EVENT_ID = "eventId"

        fun getLaunchIntent(context: Context, eventId: String): Intent {
            val intent = Intent(context, EventLabelsActivity::class.java)
            intent.putExtra(ARG_EVENT_ID, eventId)
            return intent
        }
    }
}