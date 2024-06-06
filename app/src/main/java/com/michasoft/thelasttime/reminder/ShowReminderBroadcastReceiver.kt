package com.michasoft.thelasttime.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.michasoft.thelasttime.LastTimeApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShowReminderBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var showReminderUseCase: ShowReminderUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        val reminderId = intent?.getStringExtra(REMINDER_ID) ?: return
        (context?.applicationContext as LastTimeApplication?)?.userSessionComponent?.inject(this)
        CoroutineScope(Dispatchers.Main).launch {
            showReminderUseCase.invoke(reminderId)
        }
    }

    companion object {
        const val REMINDER_ID = "reminderId"
    }
}