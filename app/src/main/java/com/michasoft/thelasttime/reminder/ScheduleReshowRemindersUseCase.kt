package com.michasoft.thelasttime.reminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleReshowRemindersUseCase @Inject constructor(
    private val context: Context,
) {
    fun invoke() {
        val scheduleTime = LocalTime.parse("09:00", DateTimeFormat.forPattern("HH:mm"))
        var scheduleDateTime = DateTime.now().withTime(scheduleTime)
        if (scheduleDateTime.isBeforeNow) {
            scheduleDateTime = scheduleDateTime.plusDays(1)
        }
        val duration = Duration(DateTime.now(), scheduleDateTime)
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "ScheduleReshowRemindersWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<ReshowRemindersWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(duration.millis, TimeUnit.MILLISECONDS)
                .build()
        )
    }
}