package com.example.hiltworkerroom.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.hiltworkerroom.R

object NotificationHandler {
    fun showNotification(
        context: Context,
        notificationId:Int,
        channelId: String,
        channelName: String,
        title: String,
        content: String
    ) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        createNotificationChannel(notificationManager, channelId, channelName)

        notificationManager.notify(notificationId, builder.build())
    }

    fun dismissNotification(
        context: Context,
        notificationId: Int
    ){
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(notificationId)
    }

    private fun createNotificationChannel(
        notificationManager: NotificationManager,
        channelId: String,
        name: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId,
                    name,
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManager.createNotificationChannel(channel)
        }
    }
}