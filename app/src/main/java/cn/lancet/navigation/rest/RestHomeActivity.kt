package cn.lancet.navigation.rest

import android.app.AppOpsManager
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import cn.lancet.navigation.AnimMessage
import cn.lancet.navigation.AnimationManager
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityRestHomeBinding
import cn.lancet.navigation.dp
import cn.lancet.navigation.module.RestType


class RestHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestHomeBinding

    private lateinit var viewModel: NoticeViewModel

    private lateinit var mAdapter: RestHomeAdapter

    private var mData = mutableListOf<RestType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[NoticeViewModel::class.java]

        binding.ivBack.setOnClickListener { finish() }

        AnimationManager.init(this)
        AnimationManager.addGiftContainer(binding.llGiftContainer)

        initEvent()
    }

    override fun onDestroy() {
        AnimationManager.release()
        super.onDestroy()
    }

    private fun initEvent() {
        mData.clear()
        mData.add(RestType().apply {
            restId = 1
            name = "植物识别"
            url = "https://cn.bing.com/images/search?q=%E5%9B%BE%E7%89%87&FORM=IQFRBA&id=457EC80FCD5EE9AB67B2B3E8F5624312D6F6400B"
            type = 0
        })
        mData.add(RestType().apply {
            restId = 2
            name = "果蔬识别"
            url = "https://cn.bing.com/images/search?q=%E5%9B%BE%E7%89%87&FORM=IQFRBA&id=4929EB0212CFAC8CB6AB59DB53A9D2D99C54FF6A"
            type = 1
        })
        mData.add(RestType().apply {
            restId = 3
            name = "动物识别"
            url = "https://cn.bing.com/images/search?q=%E5%9B%BE%E7%89%87&FORM=IQFRBA&id=21679CBB0E9648E00603E35CB617428F7F6FB48B"
            type = 2
        })
        mData.add(RestType().apply {
            restId = 4
            name = "菜品识别"
            url = "https://cn.bing.com/images/search?view=detailV2&ccid=mH9YLFEL&id=457EC80FCD5EE9AB67B2B3E8F5624312D6F6400B&thid=OIP.mH9YLFEL5YdVxJM82mjVJQHaEo&mediaurl=https%3a%2f%2fts1.cn.mm.bing.net%2fth%2fid%2fR-C.987f582c510be58755c4933cda68d525%3frik%3dC0D21hJDYvXosw%26riu%3dhttp%253a%252f%252fimg.pconline.com.cn%252fimages%252fupload%252fupc%252ftx%252fwallpaper%252f1305%252f16%252fc4%252f20990657_1368686545122.jpg%26ehk%3dnetN2qzcCVS4ALUQfDOwxAwFcy41oxC%252b0xTFvOYy5ds%253d%26risl%3d%26pid%3dImgRaw%26r%3d0&exph=1600&expw=2560&q=%e5%9b%be%e7%89%87&simid=608032842623746807&FORM=IRPRST&ck=64AA5AA7AF596CB282CA07D732152DDF&selectedIndex=1"
            type = 3
        })
        mData.add(RestType().apply {
            restId = 5
            name = "车型识别"
            url = "https://hzkyk.obs.cn-east-3.myhuaweicloud.com:443/funnyWorld%2F10060606%2FPERFEC_INFO?random=7507"
            type = 4
        })

        mAdapter = RestHomeAdapter(this).apply {
            setListener(object : RestHomeAdapter.OnRestTypeClickListener {
                override fun onItemClick(restType: RestType) {
                    val intent = Intent(this@RestHomeActivity,RestActivity::class.java)
                    intent.putExtra(Constant.KEY_REST_TYPE,restType.type)
                    startActivity(intent)
//                    finish()
                }
            })
        }
        binding.rvRest.layoutManager = GridLayoutManager(this, 2)
        binding.rvRest.adapter = mAdapter
        mAdapter.setData(mData)

        binding.tvAdd.setOnClickListener {

            val layoutParams = binding.llGiftContainer.layoutParams
            layoutParams.height = 200

            layoutParams.width = MATCH_PARENT
            binding.llGiftContainer.layoutParams = layoutParams
            binding.llGiftContainer.requestLayout()


            /*
                        for (i in 0..50){
                            when(i.mod(5)){
                                0->{
                                    AnimationManager.addAnimalMessage(AnimMessage(userName = "一无所有", giftId = i, headUrl = "", giftNum = 10, giftName = "飞机"))
                                }
                                1->{
                                    AnimationManager.addAnimalMessage(AnimMessage(userName = "普通家庭", giftId = i, headUrl = "", giftNum = 30, giftName = "游艇"))
                                }
                                2->{
                                    AnimationManager.addAnimalMessage(AnimMessage(userName = "悔创阿里", giftId = i, headUrl = "", giftNum = 50, giftName = "火箭"))
                                }
                                3->{
                                    AnimationManager.addAnimalMessage(AnimMessage(userName = "大碗宽面", giftId = i, headUrl = "", giftNum = 70, giftName = "跑车"))
                                }
                                4->{
                                    AnimationManager.addAnimalMessage(AnimMessage(userName = "姓丁名磊字三石", giftId = i, headUrl = "", giftNum = 90, giftName = "麋鹿"))
                                }
                                else->{
                                    AnimationManager.addAnimalMessage(AnimMessage(userName = "张朝阳", giftId = i, headUrl = "", giftNum = 100, giftName = "圣诞帽"))
                                }
                            }
                        }
            */


//            AnimationManager.addAnimalMessage(AnimMessage(userName = "小黑子", headUrl = "", giftNum = 10, giftName = "飞机"))


/*            mAdapter.insertData(RestType(
                restId = 6,
                "户型识别",
                url = "https://hzkyk.obs.cn-east-3.myhuaweicloud.com:443/funnyWorld%2F10072725%2F__IMAGE__PERFEC_INFO_AVATAR.jpeg?t=1683855851443",
                type = 6
            ))
            binding.rvRest.scrollToPosition(0)*/
        }


        binding.tvUpdate.setOnClickListener {

            val layoutParams = binding.llGiftContainer.layoutParams
            layoutParams.height = 400
            layoutParams.width = MATCH_PARENT
            binding.llGiftContainer.layoutParams = layoutParams
            binding.llGiftContainer.requestLayout()

//            AnimationManager.addAnimalMessage(AnimMessage(userName = "Jacky", headUrl = "", giftNum = 20, giftName = "红包"))

        }

        binding.tvDelete.setOnClickListener {

            AnimationManager.addAnimalMessage(AnimMessage(userName = "我是学生", headUrl = "", giftNum = 10, giftName = "飞机"))


//            mAdapter.moveData()
//            mAdapter.deleteData(restId = 6)
//            binding.rvRest.scrollToPosition(0)
//
//            if (isSupportPipMode()){
//                if (hasPipPermission()){
//                    enterPiPMode(Rational(3,5))
//                }else{
//                    startPipPermissionSetting()
//                }
//            }


        }

    }

    fun isSupportPipMode(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && packageManager?.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) == true
    }


    fun hasPipPermission():Boolean {
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager? ?: return false
        val uid = android.os.Process.myUid()

        return if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            appOpsManager.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE,uid,packageName) == AppOpsManager.MODE_ALLOWED
        }else{
            appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE,uid,packageName) == AppOpsManager.MODE_ALLOWED

        }
    }


    fun startPipPermissionSetting(){
        try {
            startActivity(Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS"))
        }catch (e:Exception){

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun enterPiPMode(aspectRatio: Rational){
        val pictureInPictureParams = PictureInPictureParams.Builder()
            .setAspectRatio(aspectRatio)
            .build()
        enterPictureInPictureMode(pictureInPictureParams)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode){

        }else{

        }
    }

}