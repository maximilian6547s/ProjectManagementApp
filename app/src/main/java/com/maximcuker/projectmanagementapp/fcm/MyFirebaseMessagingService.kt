package com.maximcuker.projectmanagementapp.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.maximcuker.projectmanagementapp.R
import com.maximcuker.projectmanagementapp.activities.MainActivity
import com.maximcuker.projectmanagementapp.activities.SignInActivity
import com.maximcuker.projectmanagementapp.firebase.FirestoreClass
import com.maximcuker.projectmanagementapp.utils.Constants

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        super.onMessageReceived(remoteMessage)
        Log.d(TAG , "FROM: ${remoteMessage.from}")

        if(remoteMessage.data.isNotEmpty()) {
            Log.d(TAG , "Message data Payload: ${remoteMessage.data}")
            val title = remoteMessage.data[Constants.FCM_KEY_TITLE]
            val message = remoteMessage.data[Constants.FCM_KEY_MESSAGE]

            if (message != null) {
                if (title != null) {
                    sendNotification(title,message)
                }
            }
        }

        remoteMessage.notification?.let {
            Log.d(TAG , "Message notification body: ${it.body}")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG , "Refresh token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token:String?) {
        //Implement
    }

    private fun sendNotification(title:String, message: String) {

        val intent = if (FirestoreClass().getCurrentUserId()?.isNotEmpty() == true) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, SignInActivity::class.java)
        }

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = resources.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(
            this, channelId
        ).setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel projmanapp title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object{
        private const val TAG = "MyFirebaseMsgService"
    }

}