package cn.lancet.navigation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class KeepLiveService():Service() {

    private var mNotificationManager:NotificationManager?=null
    private var mNotificationId = "keep_app_live"
    private var mNotificationName = "正在运行中..."

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel(mNotificationId,mNotificationName,NotificationManager.IMPORTANCE_HIGH).apply {
                enableVibration(false)
                setSound(null,null)
                setShowBadge(true)
            }
            mNotificationManager?.createNotificationChannel(channel)
        }
        startForeground(1,getNotification())
    }

    private fun getNotification():Notification{
        val builder = NotificationCompat.Builder(this,mNotificationId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Walden")
                .setContentIntent(getIntent())
                .setContentText("正在运行中...")
                .setAutoCancel(true)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            builder.setChannelId(mNotificationId)
        }
        return builder.build()
    }


    private fun getIntent():PendingIntent {
        val intent = applicationContext?.packageManager?.getLaunchIntentForPackage(packageName)
        return PendingIntent.getActivity(applicationContext,1,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }

}