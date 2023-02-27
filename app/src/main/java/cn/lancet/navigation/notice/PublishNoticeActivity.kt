package cn.lancet.navigation.notice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UploadFileListener
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityPublishNoticeBinding
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.util.AppPreUtils
import cn.lancet.navigation.util.FileUtils
import com.hjq.toast.Toaster
import kotlinx.coroutines.launch


class PublishNoticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublishNoticeBinding

    private var mUri:Uri?=null

    private lateinit var viewModel: NoticeViewModel

    private val launcherActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            mUri = it.data?.data
            binding.ivCover.setImageURI(mUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPublishNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory())[NoticeViewModel::class.java]

        binding.ivBack.setOnClickListener { finish() }

        initEvent()
    }

    private fun initEvent() {

        binding.etNoticeTitle.addTextChangedListener {
            binding.btnPublish.isEnabled =
                binding.etNoticeContent.text?.isBlank() != true
                    && it?.isBlank() != true
        }

        binding.etNoticeContent.addTextChangedListener {
            binding.btnPublish.isEnabled =
                binding.etNoticeTitle.text?.isBlank() != true
                        && it?.isBlank() != true
        }

        binding.ivCover.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            launcherActivity.launch(intent)
        }

        binding.btnPublish.setOnClickListener {
            mUri?.let { it1 -> uploadNoticeCoverImage(it1) }
        }

        lifecycleScope.launch {
            viewModel.addFlow.collect{
                Toaster.showLong("发布成功")
                finish()
            }
        }

    }

    private fun uploadNoticeCoverImage(uri:Uri){

        var fileUrl = ""

        var imagePathFile = FileUtils.getFileFromUri(this,uri)

        val bmobFile = BmobFile(imagePathFile)

        val notice = Notice(
            createdTime = System.currentTimeMillis(),
            updatedTime = System.currentTimeMillis(),
            coverImageUrl = fileUrl,
            noticeTitle = binding.etNoticeTitle.text.toString(),
            noticeContent = binding.etNoticeContent.text.toString(),
            publisher = AppPreUtils.getString(Constant.KEY_USER_NAME))

        bmobFile.uploadblock(object : UploadFileListener() {
            override fun done(e: BmobException?) {
                if (e == null){
                    fileUrl = bmobFile.fileUrl
                }
            }
        })

        viewModel.addNotice(notice.copy(coverImageUrl = fileUrl))

    }

}