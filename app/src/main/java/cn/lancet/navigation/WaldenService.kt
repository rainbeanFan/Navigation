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

class WaldenService:Service() {

    private var mNotificationManager:NotificationManager?=null
    private var mNotificationId = "keep_app_live"
    private var mNotificationName =  "Walden is running..."

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel(mNotificationId,mNotificationName,NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(false)
            channel.setSound(null,null)
            mNotificationManager?.createNotificationChannel(channel)
        }
        startForeground(1,getNotification())
    }

    private fun getNotification():Notification {
        val builder = Notification.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Walden")
            .setContentIntent(getIntent())
            .setContentText("Running background...")
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            builder.setChannelId(mNotificationId)
        }
        return builder.build()
    }

    private fun getIntent():PendingIntent {

        val intent = applicationContext.packageManager.getLaunchIntentForPackage(packageName)

        return PendingIntent.getActivity(applicationContext,1,intent,PendingIntent.FLAG_IMMUTABLE)

    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }


}