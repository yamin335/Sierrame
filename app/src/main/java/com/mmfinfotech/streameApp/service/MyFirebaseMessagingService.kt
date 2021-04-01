package com.mmfinfotech.streameApp.service

import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.NotificationActivity
import com.mmfinfotech.streameApp.dashBoard.activity.PostDescriptionActivity
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG: String? = "MyFirebaseMessagingService"

    //    private var notifManager: NotificationManager? = null
//    private val mChannel: NotificationChannel? = null
    private var appPreferences: AppPreferences? = null

    private var notificationManager: NotificationManager? = null
    private var notificationChannel: NotificationChannel? = null
    private var builder: Notification.Builder? = null
    private val channelId = "com.mmfinfotech.streameApp.notifications"
    private val description = "Test notification"

    override fun onNewToken(tokenString: String) {
        super.onNewToken(tokenString)
        if (appPreferences == null) appPreferences = AppPreferences()
        Log.v(TAG, "New FCM Token $tokenString")
        appPreferences?.setFcmToken(applicationContext, tokenString)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.v(TAG, "Remote Message 1 ==> $remoteMessage")
        //{channel_id=f3186825-b982-484b-875c-d3a7aedf598a, reference_id=328, access_option=1, body=sonam123 has Started a live streaming., icon=new, type=4, sound=default, title=Notification}
//        jab Liver dusre ko add karne ke liye request karta hai to...
//        {reference_id=376, body=sonam123 invited you to join live stream., icon=new, type=1, sound=default, title=Notification}
//        Jab samne wale ne invitation accept kar liya to ye notification wapas ata hai
//        {reference_id=414, body=queen has accepted your invitation for join stream, icon=new, type=2, sound=default, title=Notification}
        Log.v(TAG, "Remote Message 2 ==> ${remoteMessage.data}")
        Log.v(TAG, "Remote Message 3 ==> ${remoteMessage.data["body"]}")
        Log.v(TAG, "Remote Message 4 ==> ${remoteMessage.data["access_option"]}") //1 = public, 2 = private
        Log.v(TAG, "Remote Message 5 ==> ${remoteMessage.data["type"]}") // 4 = live
        Log.v(TAG, "Remote Message 6 ==> ${remoteMessage.data["channel_id"]}")
        Log.v(TAG, "Remote Message 7 ==> ${remoteMessage.data["reference_id"]}")
        Log.v(TAG, "Remote Message app in foreground ==> ${appInForeground(this@MyFirebaseMessagingService)}")

        val type = remoteMessage.data["type"]
        when (type) {
            "1" -> {
                val intent = Intent().apply {
                    action = "InvitationToJoinLive"
                    putExtra("referenceId", remoteMessage.data["reference_id"])
                    putExtra("message", remoteMessage.data["body"])
                }
                sendBroadcast(intent)
//                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
            "2" -> {

            }
            else -> {
                sendNotification(remoteMessage.notification?.body.toString(), "Streame App", remoteMessage)
            }
        }
    }

//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//
//        Log.d("TAG", "From: " + remoteMessage.getFrom());
//        Log.v(TAG, "Remote Message 1 ==> $remoteMessage")
//        Log.v(
//            TAG,
//            "Remote Message 2 ==> ${remoteMessage.data}"
//        ) //{channel_id=f3186825-b982-484b-875c-d3a7aedf598a, reference_id=328, access_option=1, body=sonam123 has Started a live streaming., icon=new, type=4, sound=default, title=Notification}
//        Log.v(TAG, "Remote Message 3 ==> ${remoteMessage.data["body"]}")
//        Log.v(TAG, "Remote Message 4 ==> ${remoteMessage.data["access_option"]}") //1 = public, 2 = private
//        Log.v(TAG, "Remote Message 5 ==> ${remoteMessage.data["type"]}") // 4 = live
//        Log.v(TAG, "Remote Message 6 ==> ${remoteMessage.data["channel_id"]}")
//        Log.v(TAG, "Remote Message 7 ==> ${remoteMessage.data["reference_id"]}")
//        Log.v(TAG, "Remote Message app in foreground ==> ${appInForeground(this@MyFirebaseMessagingService)}")
//
//        if (remoteMessage.data.size > 0) {
//            Log.d("TAG", "Message data payload: " + remoteMessage.getData())
//        }
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d("TAG", "Message Notification Body: " + remoteMessage.notification?.body)
//        }
//
//        val type = remoteMessage.data["type"]
//        if ("1".equals(type, ignoreCase = true)) {
//            val intent = generateIntent1(remoteMessage)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//        } else if ("2".equals(type, ignoreCase = true)) {
////            val intent = generateIntent1(remoteMessage)
////            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//        } else {
//            sendNotification(remoteMessage.notification?.body.toString(), "Streame App", remoteMessage)
//        }
//
////        sendPrivateNotification(remoteMessage.notification?.body, "Streame App", "")
//    }

    private fun generateIntent1(remoteMessage: RemoteMessage): Intent {
        //vibrator.cancel();

        val type: String? = remoteMessage.data["type"]
        val refrenceId = remoteMessage.data["reference_id"]
        val body = remoteMessage.data["body"]


        val intent = Intent(this, NotificationActivity::class.java)
        intent.action = AppConstants.IntentActions.actionNotification
        intent.putExtra(AppConstants.IntentExtras.NotifictnRemoteRefrenceId, refrenceId)
        intent.putExtra(AppConstants.IntentExtras.NotifictnRemoteMsg, body)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        return intent
    }

    private fun generateIntent(remoteMessage: RemoteMessage): Intent {
//        val intent = Intent(this, NotificationActivity::class.java)
//        intent.action = AppConstants.IntentActions.actionNotification
//        intent.putExtra(AppConstants.IntentExtras.NotifictnRemoteRefrenceId, refrenceId)
//        intent.putExtra(AppConstants.IntentExtras.NotifictnRemoteMsg, body)
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        return intent
        return if ("4".equals(remoteMessage.data["type"], ignoreCase = true) || "5".equals(remoteMessage.data["type"], ignoreCase = true)) {
            PostDescriptionActivity.getInstance(this@MyFirebaseMessagingService, remoteMessage.data["reference_id"]).apply {
                action = AppConstants.IntentActions.actionNotification
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } else
            Intent(this@MyFirebaseMessagingService, NotificationActivity::class.java).apply {
                action = AppConstants.IntentActions.actionNotification
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
    }

    private fun sendNotification(message: String, title: String, remoteMessage: RemoteMessage) {
        val intent = generateIntent(remoteMessage)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (notificationManager == null) {
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel?.enableLights(true)
            notificationChannel?.lightColor = Color.GREEN
            notificationChannel?.enableVibration(false)
            notificationManager?.createNotificationChannel(notificationChannel!!)

            builder = Notification.Builder(this, channelId)
//                .setContent(contentView)
                .setContentTitle(title)
                .setContentText(message)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.icon)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
        } else {

            builder = Notification.Builder(this)
//                .setContent(contentView)
                .setContentTitle(title)
                .setContentText(message)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.icon)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
        }
        notificationManager?.notify(1234, builder?.build())
    }


    /*private fun sendNotification(message: String, title: String, remoteMessage: RemoteMessage) {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        assert(vibrator != null)

        val intent = generateIntent(remoteMessage)

        //vibrator.cancel();
        val pendingIntent: PendingIntent
        val builder: NotificationCompat.Builder
        if (notifManager == null) {
            notifManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            if (mChannel == null) {
                val mChannel = NotificationChannel("0", title, importance)
                mChannel.description = message
                mChannel.shouldVibrate()
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                //mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notifManager!!.createNotificationChannel(mChannel)
            }
            builder = NotificationCompat.Builder(this, "0")
            intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentTitle(title)
                .setSmallIcon(R.drawable.icon)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .setPriority(Notification.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
        } else {
            builder = NotificationCompat.Builder(this)
            intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentTitle(title)
                .setSmallIcon(R.drawable.icon) // required
                .setColor(resources.getColor(R.color.colorAccent))
                .setContentText(message) // required
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)).priority =
                Notification.PRIORITY_HIGH
        }
        val notification = builder.build()
        notifManager!!.notify(0, notification)
    }*/

    private fun appInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses ?: return false
        for (runningAppProcess in runningAppProcesses) {
            if (runningAppProcess.processName == context.getPackageName() &&
                runningAppProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            ) {
                return true
            }
        }
        return false
    }

}