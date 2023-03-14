package cn.lancet.navigation.ui.me

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FetchUserInfoListener
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.Base64Util
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.net.URLEncoder


class MeViewModel() : ViewModel() {

    val sharedFlow = MutableSharedFlow<User>()

    val userAvatarFlow = MutableSharedFlow<UserAvatar>()

    fun getUserInfo() {

        BmobUser.fetchUserInfo(object : FetchUserInfoListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                if (e == null) {
                    user?.let {
                        viewModelScope.launch {
                            sharedFlow.emit(it)
                        }
                    }
                }
            }

        })

    }

    fun modifyAvatar(path: String) {

        val baseUrl = "https://aip.baidubce.com/rest/2.0/image-process/v1/selfie_anime"

        val imgData = File(path).readBytes()
//            FileUtil.readFileByBytes(path)
        val imgStr = Base64Util.encode(imgData)
        val imgParam = "image=" + URLEncoder.encode(imgStr, "UTF-8")

        val url = "$baseUrl?access_token=${Constant.KEY_BD_ACCESS_TOKEN}"
        val client: OkHttpClient = OkHttpClient.Builder()
            .build()

        val requestBody =
            imgParam.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())

        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("Connection", "Keep-Alive")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val str = response.body!!.string()
                    val userAvatar = gson.fromJson(str, UserAvatar::class.java)

                    Log.d("BDAI onResponse ", str)

                    viewModelScope.launch() {
                        userAvatarFlow.emit(userAvatar)
                    }
                }
            }
        })
    }

}

