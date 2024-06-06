package com.michasoft.thelasttime.model.reminder

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.michasoft.thelasttime.reminder.ShowReminderBroadcastReceiver
import com.michasoft.thelasttime.util.isNotAfterNow
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

abstract class Reminder(
    val id: String,
    val eventId: String,
    val triggerDateTime: DateTime?,
    val reshowEnabled: Boolean
) {
    abstract val label: String
    abstract val type: Type

    val isTriggerDateTimePassed: Boolean
        get() = triggerDateTime?.isNotAfterNow == true

    enum class Type {
        Single,
        Repeated
    }

    fun createAlarmPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ShowReminderBroadcastReceiver::class.java)
            .putExtra(ShowReminderBroadcastReceiver.REMINDER_ID, id)
            .setData(Uri.parse(id))
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    protected fun createLabel(dateTime: DateTime): String {
        if (dateTime.year != DateTime.now().year) {
            return dateTime.toString(labelFullDatetimeFormatter)
        }
        if (dateTime.dayOfYear() == DateTime.now().dayOfYear()) {
            return "Today, " + dateTime.toString(labelTimeFormatter)
        }
        if (dateTime.dayOfYear() == DateTime.now().plusDays(1).dayOfYear()) {
            return "Tomorrow, " + dateTime.toString(labelTimeFormatter)
        }
        return dateTime.toString(labelDatetimeFormatter)
    }

    companion object {
        private val labelTimeFormatter = DateTimeFormat.forPattern("HH:mm")
        private val labelDatetimeFormatter = DateTimeFormat.forPattern("dd MMM, HH:mm")
        private val labelFullDatetimeFormatter = DateTimeFormat.forPattern("dd MMM yyyy, HH:mm")
    }
}