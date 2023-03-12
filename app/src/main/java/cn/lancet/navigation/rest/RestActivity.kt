package cn.lancet.navigation.rest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.PlantInfoAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityPublishNoticeBinding
import cn.lancet.navigation.db.DBManager
import cn.lancet.navigation.db.RestResultEntity
import cn.lancet.navigation.module.RestResultInfo
import cn.lancet.navigation.module.User
import cn.lancet.navigation.net.PlantInfoRes
import cn.lancet.navigation.util.FileUtils
import cn.lancet.navigation.widget.SelectPictureBottomDialog
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublishNoticeBinding

    private var mUri: Uri? = null

    private lateinit var viewModel: NoticeViewModel

    private lateinit var mAdapter: PlantInfoAdapter

    private var mRestType = 0

    private val launcherActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            mUri = it.data?.data
            binding.ivCover.setImageURI(mUri)
            val filePath = FileUtils.getFilePath(this, mUri)
            viewModel.getRestInfo(mRestType, filePath)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPublishNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mRestType = intent.getIntExtra(Constant.KEY_REST_TYPE, 0)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[NoticeViewModel::class.java]
        initEvent()
    }

    private fun initEvent() {
        binding.ivBack.setOnClickListener { finish() }

        binding.tvTitle.text =
            when (mRestType) {
                1 -> "果蔬识别"
                2 -> "动物识别"
                3 -> "菜品识别"
                4 -> "汽车识别"
                else -> "植物识别"
            }

        mAdapter = PlantInfoAdapter(this)
        binding.rvResult.layoutManager = LinearLayoutManager(this)
        binding.rvResult.adapter = mAdapter

        binding.btnPublish.setOnClickListener {

            XXPermissions.with(this)
                .permission(
                    Permission.CAMERA,
                    Permission.WRITE_EXTERNAL_STORAGE,
                    Permission.READ_MEDIA_IMAGES,
                    Permission.READ_MEDIA_VIDEO,
                    Permission.READ_MEDIA_AUDIO
                )
                .request { permissions, allGranted ->
                    if (allGranted) {

                        SelectPictureBottomDialog()
                            .setListener(object : SelectPictureBottomDialog.OnItemClickListener {
                                override fun openCamera() {
                                    launcherActivity.launch(
                                        Intent(
                                            this@RestActivity,
                                            PreviewActivity::class.java
                                        )
                                    )
                                }

                                override fun openGallery() {
                                    val intent = Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    )
                                    launcherActivity.launch(intent)
                                }
                            })
                            .show(supportFragmentManager, "SELECT_PHOTO_BOTTOM")
                    }

                }

        }

        lifecycleScope.launch {
            viewModel.addFlow.collect {
                Toaster.showLong("发布成功")
                finish()
            }
        }

        lifecycleScope.launch {
            viewModel.plantInfoFlow.collect { it ->
                it?.let { plantInfo ->
                    Toaster.showLong(it.toString())
                    plantInfo.result?.let {
                        mAdapter.setData(it)
                        savePlant(plantInfo)
                    }
                }
            }
        }
    }

    private fun savePlant(plantInfo: PlantInfoRes) {

        val restResultEntity = RestResultEntity(
            restId = plantInfo.log_id.toString(),
            restType = mRestType,
            plantName = plantInfo.result?.get(0)?.name,
            plantUrl = plantInfo.result?.get(0)?.baike_info?.image_url,
            plantDescription = plantInfo.result?.get(0)?.baike_info?.description
        )

        lifecycleScope.launch(Dispatchers.IO){
            DBManager.instance.getDB(this@RestActivity)
                .restResultDao()
                .insertRest(restResultEntity)
        }

        if (BmobUser.isLogin()){
            val plant = RestResultInfo().apply {
                userId = BmobUser.getCurrentUser(User::class.java).objectId
                favourite = false
                plantName = plantInfo.result?.get(0)?.name
                plantUrl = plantInfo.result?.get(0)?.baike_info?.image_url
                plantDescription = plantInfo.result?.get(0)?.baike_info?.description
            }
            viewModel.addPlant(plant)
        }

    }

}