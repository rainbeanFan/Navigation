package cn.lancet.navigation.rest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.module.RestResultInfo
import cn.lancet.navigation.net.PlantInfoRes
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


class NoticeViewModel() : ViewModel() {

    var noticeFlow = MutableSharedFlow<Notice>()
    var deleteFlow = MutableSharedFlow<Boolean>()
    var addFlow = MutableSharedFlow<Notice>()
    var plantInfoFlow = MutableSharedFlow<PlantInfoRes?>()
    var addPlantFlow = MutableSharedFlow<RestResultInfo>()
    fun getNoticeDetail(restId: String) {
        val queries = BmobQuery<Notice>()
        queries.getObject(restId, object : QueryListener<Notice>() {
            override fun done(notice: Notice?, e: BmobException?) {
                if (e == null) {
                    viewModelScope.launch {
                        notice?.let {
                            noticeFlow.emit(it)
                        }
                    }
                }
            }
        })
    }

    fun deleteNotice(noticeId: String) {
        val notice = Notice()
        notice.delete(noticeId, object : UpdateListener() {
            override fun done(e: BmobException?) {
                viewModelScope.launch {
                    deleteFlow.emit(e == null)
                }
            }
        })
    }

    fun getRestInfo(restType: Int, path: String) {

        val baseUrl =
            when (restType) {
                1 -> "https://aip.baidubce.com/rest/2.0/image-classify/v1/classify/ingredient"
                2 -> "https://aip.baidubce.com/rest/2.0/image-classify/v1/animal"
                3 -> "https://aip.baidubce.com/rest/2.0/image-classify/v2/dish"
                4 -> "https://aip.baidubce.com/rest/2.0/image-classify/v1/car"
                else -> "https://aip.baidubce.com/rest/2.0/image-classify/v1/plant"
            }


        val imgData = File(path).readBytes()
//            FileUtil.readFileByBytes(path)
        val imgStr = Base64Util.encode(imgData)
        val imgParam = "image=" + URLEncoder.encode(imgStr, "UTF-8")

        val url = "$baseUrl?access_token=${Constant.KEY_BD_ACCESS_TOKEN}"
        val client: OkHttpClient = OkHttpClient.Builder()
            .build()

        val requestBody =
            when (restType) {
                1 -> "${imgParam}&baike_num=3".toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
                2 -> "${imgParam}&baike_num=3".toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
                3 -> "${imgParam}&baike_num=3".toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
                4 -> "${imgParam}&baike_num=3".toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
                else -> "${imgParam}&baike_num=3".toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())

            }

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

                    Log.d("BDAI onResponse ", str)

                    val plantInfo: PlantInfoRes? =
                        gson.fromJson(str, PlantInfoRes::class.java)

                    viewModelScope.launch() {
                        plantInfo?.let { plantInfoFlow.emit(it) }
                    }
                }
            }
        })
    }

    fun addPlant(plant: RestResultInfo) {
        plant.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
                if (e == null && objectId != null) {
                    viewModelScope.launch {
                        addPlantFlow.emit(plant)
                    }
                }
            }
        })
    }
}