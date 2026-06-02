package cn.lancet.navigation.account

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.lancet.navigation.MainActivity
import cn.lancet.navigation.databinding.ActivityLoginBinding
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
            mPassword = mBinding?.etPwd?.text.toString()
            viewModel.login(mAccount, mPassword)
        }

        lifecycleScope.launch {
            viewModel.loginSharedFlow.collect {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

    }


}
