package com.michasoft.thelasttime.notification

import android.app.NotificationManager

data class NotificationChannelData(
    val id: String,
    val name: String,
    val descriptionText: String,
    val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
)