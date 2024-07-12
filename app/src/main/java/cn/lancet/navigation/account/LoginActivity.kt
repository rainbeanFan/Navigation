package cn.lancet.navigation.account

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.lancet.navigation.MainActivity
import cn.lancet.navigation.databinding.ActivityLoginBinding
import cn.lancet.navigation.util.CommonUtil
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.Toaster
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null

    private lateinit var viewModel: UserViewModel

    private var mAccount = ""
    private var mPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        ImmersionBar.with(this).init()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserViewModel::class.java]
        initEvent()
    }

    private fun initEvent() {

        mBinding?.tvSign?.setOnClickListener {
            startActivity(Intent(this, SignActivity::class.java))
        }

        mBinding?.tvAgree?.setOnClickListener {
            Toaster.show("查看用户协议")
        }

        mBinding?.btnLogin?.setOnClickListener {
            mAccount = mBinding?.etAccount?.text.toString()
            if (mAccount.isBlank() || !CommonUtil.emailIsValid(mAccount)) {
                Toaster.show("please input your email！")
                return@setOnClickListener
            }

            mPassword = mBinding?.etPwd?.text.toString()
            if (mPassword.isBlank()) {
                Toaster.show("please input your password！")
                return@setOnClickListener
            }

            if (!mBinding!!.checkbox.isChecked) {
                Toaster.show("请先同意用户协议！")
                return@setOnClickListener
            }
            viewModel.login(mAccount, mPassword)
        }

        lifecycleScope.launch {
            viewModel.loginSharedFlow.collect {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
/*
                it?.RCToken?.let {
                    IMCenter.getInstance().connect(it,0,object : RongIMClient.ConnectCallback(){
                        override fun onSuccess(userId: String?) {
                            Toaster.show("连接成功")
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }

                        override fun onError(e: RongIMClient.ConnectionErrorCode?) {
                            if (e == RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT) {
                                Toaster.show("token错误")
                            }else if (e == RongIMClient.ConnectionErrorCode.RC_CONNECT_TIMEOUT){
                                Toaster.show("连接超时")
                            }
                        }

                        override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) {
                            if (code == RongIMClient.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS){
                                Toaster.show("数据库打开成功")
                            }else{
                                Toaster.show("数据库打开失败")
                            }
                        }
                    })

                }
*/

            }
        }

    }


}