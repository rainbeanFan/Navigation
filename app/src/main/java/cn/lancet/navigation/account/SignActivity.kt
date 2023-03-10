package cn.lancet.navigation.account

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.lancet.navigation.databinding.ActivitySignBinding
import cn.lancet.navigation.util.CommonUtil
import cn.lancet.navigation.util.FileUtils
import com.gyf.immersionbar.ImmersionBar
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.Toaster
import kotlinx.coroutines.launch


class SignActivity : AppCompatActivity() {

    private var mBinding: ActivitySignBinding? = null

    private lateinit var viewModel: UserViewModel

    private var mAccount = ""
    private var mPassword = ""
    private var mAvatar = ""

    private val launcherActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val mUri = it.data?.data
            mBinding?.ivAvatar?.setImageURI(mUri)
            val filePath = FileUtils.getFilePath(this, mUri)
            viewModel.modifyAvatar(filePath)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        ImmersionBar.with(this).init()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserViewModel::class.java]
        initEvent()
    }

    private fun initEvent() {

        mBinding?.tvAgree?.setOnClickListener {
            Toaster.show("查看用户协议")
        }

        mBinding?.ivAvatar?.setOnClickListener {
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
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        launcherActivity.launch(intent)
                    }
                }
        }

        mBinding?.btnSign?.setOnClickListener {
            mAccount = mBinding?.etAccount?.text.toString()
            if (mAccount.isBlank() || !CommonUtil.emailIsValid(mAccount)) {
                Toaster.show("please input your email")
                return@setOnClickListener
            }

            mPassword = mBinding?.etPwd?.text.toString()
            if (mPassword.isBlank()) {
                Toaster.show("please input your password")
                return@setOnClickListener
            }

            if (!mBinding!!.checkbox.isChecked) {
                Toaster.show("请先同意用户协议！")
                return@setOnClickListener
            }
            viewModel.signUp(mAccount, mPassword,mAvatar)
        }


        lifecycleScope.launch {
            viewModel.signupSharedFlow.collect {
                if (it) {
                    Toaster.show("注册成功，可以登录啦！")
                    finish()
                } else {
                    Toaster.showLong("注册失败，请稍后再试!")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.userAvatarFlow.collect {
//                val decodedString = Base64.decode(it.image, Base64.DEFAULT)
//                val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                mBinding?.ivAvatar?.setImageBitmap(bitmap)
                mAvatar = it.image
            }
        }

    }


}