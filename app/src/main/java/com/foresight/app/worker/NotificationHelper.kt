package com.foresight.app.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.foresight.app.MainActivity
import com.foresight.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_EXPIRY = "expiry_reminders"
        const val CHANNEL_DAILY = "daily_digest"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val manager = context.getSystemService(NotificationManager::class.java)

        val expiryChannel = NotificationChannel(
            CHANNEL_EXPIRY,
            context.getString(R.string.notif_channel_expiry),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.notif_channel_expiry_desc)
        }

        val dailyChannel = NotificationChannel(
            CHANNEL_DAILY,
            context.getString(R.string.notif_channel_daily),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = context.getString(R.string.notif_channel_daily_desc)
        }

        manager.createNotificationChannel(expiryChannel)
        manager.createNotificationChannel(dailyChannel)
    }

    fun showExpiryNotification(itemId: Long, itemName: String, daysLeft: Int) {
        val manager = context.getSystemService(NotificationManager::class.java)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("ITEM_ID", itemId)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, itemId.toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = if (daysLeft <= 0) {
            context.getString(R.string.notif_expired_title, itemName)
        } else {
            context.getString(R.string.notif_expiring_title, itemName, daysLeft)
        }

        val body = if (daysLeft <= 0) {
            context.getString(R.string.notif_expired_body)
        } else {
            context.getString(R.string.notif_expiring_body, daysLeft, itemName)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_EXPIRY)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(itemId.toInt(), notification)
    }
}
