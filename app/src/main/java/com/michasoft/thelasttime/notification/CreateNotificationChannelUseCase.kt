package com.michasoft.thelasttime.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE

class CreateNotificationChannelUseCase(
    private val context: Context
) {

    operator fun invoke(channelData: NotificationChannelData) {
        val name = channelData.name // getString(R.string.channel_name)
        val descriptionText = channelData.descriptionText // getString(R.string.channel_description)
        val importance = channelData.importance
        val mChannel = NotificationChannel(channelData.id, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}