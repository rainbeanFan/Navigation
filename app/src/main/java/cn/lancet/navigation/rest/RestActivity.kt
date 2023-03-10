package cn.lancet.navigation.rest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.PlantInfoAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityPublishNoticeBinding
import cn.lancet.navigation.module.PlantDiscoveryInfo
import cn.lancet.navigation.module.User
import cn.lancet.navigation.net.PlantInfoRes
import cn.lancet.navigation.net.Result
import cn.lancet.navigation.util.FileUtils
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.Toaster
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

            viewModel.getRestInfo(mRestType,filePath)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPublishNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mRestType = intent.getIntExtra(Constant.KEY_REST_TYPE,0)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[NoticeViewModel::class.java]

        binding.ivBack.setOnClickListener { finish() }

        initEvent()
    }

    private fun initEvent() {

        mAdapter = PlantInfoAdapter(this)
        binding.rvResult.layoutManager = LinearLayoutManager(this)
        binding.rvResult.adapter = mAdapter

        binding.btnPublish.setOnClickListener {

            XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .permission(
                    Permission.READ_MEDIA_IMAGES,
                    Permission.READ_MEDIA_VIDEO,
                    Permission.READ_MEDIA_AUDIO
                )
                .request { permissions, allGranted ->
                    if (allGranted) {
                        val intent = Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        launcherActivity.launch(intent)
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
                it?.let {plantInfo->
                    Toaster.showLong(it.toString())
                    plantInfo.result?.let {
                        mAdapter.setData(it)
                        savePlant(plantInfo)
                    }
                }
            }
        }
    }

    private fun savePlant(plantInfo: PlantInfoRes){
        val plant = PlantDiscoveryInfo().apply {
            userId = BmobUser.getCurrentUser(User::class.java).objectId
            favourite = false
            plantName = plantInfo.result?.get(0)?.name
            plantUrl = plantInfo.result?.get(0)?.baike_info?.image_url
            plantDescription = plantInfo.result?.get(0)?.baike_info?.description
        }
        viewModel.addPlant(plant)
    }

}