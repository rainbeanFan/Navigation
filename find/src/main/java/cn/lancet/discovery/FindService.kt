package cn.lancet.discovery

import android.content.Context
import android.widget.Toast
import cn.lancet.common.IUserExitService

class FindService:IUserExitService {

    override fun exit(context: Context) {
        Toast.makeText(context,"Find Service",Toast.LENGTH_LONG).show()
    }

}