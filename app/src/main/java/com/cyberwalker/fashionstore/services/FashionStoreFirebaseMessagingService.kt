package com.cyberwalker.fashionstore.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.cyberwalker.fashionstore.R
import com.cyberwalker.fashionstore.navigation.FashionViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.cyberwalker.fashionstore.MainActivity
import com.cyberwalker.fashionstore.navigation.Screen


/**
 * By default, push notifications on Android are designed to work in the background
 * This way(and also broadcast receivers), your app can respond to the push notification and perform any necessary
 * actions while the user is actively using the app.
 *
 * Q1: How can I get a viewModel instance on this service?
 * Q2: I've sent custom data in the notification, How can I handle the onMessageReceived when app is on background?
 */

@AndroidEntryPoint
class FashionStoreFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Firebase Token", "Token: $token")
        //callBackendServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "Nilesh_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Your Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.description = "Description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            channel.canBypassDnd()
        }

        val data = message.data


        // Build the Intent to launch the appropriate Activity or Fragment
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Extract custom data from the notification
        intent.putExtra("custom_data_key", data["custom_data_key"])



        // Create the PendingIntent to be used when the user taps on the notification
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent,  PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        notificationBuilder.setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.black))
            .setContentTitle(message.notification!!.title)
            .setContentText(message.notification!!.body)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        Log.d("Firebase Token", "onMessageReceived: ${message.data}")

        notificationManager.notify(1000, notificationBuilder.build())

        super.onMessageReceived(message)
    }
}