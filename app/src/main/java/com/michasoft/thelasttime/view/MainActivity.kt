package com.michasoft.thelasttime.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.michasoft.thelasttime.LastTimeApplication
import com.michasoft.thelasttime.R
import com.michasoft.thelasttime.notification.CreateNotificationChannelUseCase
import com.michasoft.thelasttime.notification.CreateReminderNotificationUseCase
import com.michasoft.thelasttime.notification.ShowNotificationUseCase
import com.michasoft.thelasttime.reminder.ReshowRemindersUseCase
import com.michasoft.thelasttime.reminder.ScheduleReminderUseCase
import com.michasoft.thelasttime.reminder.ScheduleReshowRemindersUseCase
import com.michasoft.thelasttime.repo.BackupRepository
import com.michasoft.thelasttime.repo.EventRepository
import com.michasoft.thelasttime.repo.ReminderRepository
import com.michasoft.thelasttime.repo.UserSessionRepository
import com.michasoft.thelasttime.util.BackupConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainActivity : UserSessionActivity() {
    @Inject
    lateinit var eventRepository: EventRepository

    @Inject
    lateinit var backupRepository: BackupRepository

    @Inject
    lateinit var backupConfig: BackupConfig

    @Inject
    lateinit var userSessionRepository: UserSessionRepository

    @Inject
    lateinit var createNotificationChannelUseCase: CreateNotificationChannelUseCase

    @Inject
    lateinit var showNotificationUseCase: ShowNotificationUseCase

    @Inject
    lateinit var createReminderNotificationUseCase: CreateReminderNotificationUseCase

    @Inject
    lateinit var scheduleReminderUseCase: ScheduleReminderUseCase

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var reshowRemindersUseCase: ReshowRemindersUseCase

    @Inject
    lateinit var scheduleReshowRemindersUseCase: ScheduleReshowRemindersUseCase

    override fun onActivityCreate(savedInstanceState: Bundle?) {
        (application as LastTimeApplication).userSessionComponent!!.inject(this)
        setContentView(R.layout.activity_main)
        val switch1 =
            findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switch1)
        CoroutineScope(Dispatchers.IO).launch {
            val autoBackup = backupConfig.isAutoBackup()
            withContext(Dispatchers.Main) {
                switch1.isChecked = autoBackup
            }
        }

        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                backupConfig.setAutoBackup(isChecked)
            }
        }
    }

    fun clearLocal(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            backupRepository.clearLocalDatabase()
        }
    }

    fun restoreBackup(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            backupRepository.restoreBackup()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "restore backup finished", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun reshowReminders(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            reshowRemindersUseCase.invoke()
        }
    }


    fun eventList(view: View) {
        startActivity(
            Intent(
                this,
                com.michasoft.thelasttime.eventList.EventListActivity::class.java
            ), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    fun logout(view: View) {
        runBlocking {
            userSessionRepository.logout()
        }
        Intent(this, LoginActivity::class.java).also {
            startActivity(it, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
        }
        finishAfterTransition()
    }

    fun createChannelNotification(view: View) {
    }

    fun showNotification(view: View) {
        CoroutineScope(Dispatchers.IO).launch {
            scheduleReshowRemindersUseCase.invoke()
        }
//        val batterySaverIntent = Intent()
//        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS))

//        val intent = Intent()
//        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        val uri = Uri.fromParts("package", this.packageName, null)
//        intent.setData(uri)
//        startActivity(intent)

//        batterySaverIntent.component = ComponentName(
//            "com.android.settings",
//            "com.android.settings.Settings\$BatterySaverSettingsActivity"
//        )
//        startActivityForResult(batterySaverIntent, 0)
    }
}