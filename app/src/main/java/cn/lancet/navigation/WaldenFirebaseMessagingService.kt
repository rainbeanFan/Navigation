package cn.lancet.navigation

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WaldenFirebaseMessagingService:FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("Lancet=====","${message.notification?.body}")
        Log.d("Lancet=====", message.data.toString())

        if (message.data.isNotEmpty()){

            message.data.entries.forEach {
                it.key
                it.value
            }

        }

        if (message.notification!=null){
            val title = message.notification?.title
            message.notification?.body
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}


