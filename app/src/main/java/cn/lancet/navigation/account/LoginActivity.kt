package cn.lancet.navigation.account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.lancet.navigation.MainActivity
import cn.lancet.navigation.databinding.ActivityLoginBinding
import com.hjq.toast.Toaster
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserViewModel::class.java]
        initEvent()
    }

    private fun initEvent() {

        mBinding?.btnLogin?.setOnClickListener {

            val account = mBinding?.etAccount?.text.toString()
            if (account.isBlank()) {
                Toast.makeText(this, "please input your account", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val password = mBinding?.etPwd?.text.toString()
            if (password.isBlank()) {
                Toast.makeText(this, "please input your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(account, password)
        }

        lifecycleScope.launch {
            viewModel.loginSharedFlow.collect {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

        lifecycleScope.launch {
            viewModel.signupSharedFlow.collect {
                if (it) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toaster.showLong("Login failed!")
                }
            }
        }
    }


}