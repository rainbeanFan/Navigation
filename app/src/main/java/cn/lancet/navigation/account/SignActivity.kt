package cn.lancet.navigation.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.lancet.navigation.databinding.ActivitySignBinding
import cn.lancet.navigation.util.CommonUtil
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.Toaster
import kotlinx.coroutines.launch


class SignActivity : AppCompatActivity() {

    private var mBinding: ActivitySignBinding? = null

    private lateinit var viewModel: UserViewModel

    private var mAccount = ""
    private var mPassword = ""

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
            viewModel.signUp(mAccount, mPassword)
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
    }


}