package cn.lancet.me

import android.content.Context
import android.content.Intent
import cn.lancet.common.IUserInstallService

class UserInstallService:IUserInstallService {
    override fun launch(context: Context, extra: String) {
        val intent = Intent(context,SettingsActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("extra_data",extra)
        context.startActivity(intent)
    }

}