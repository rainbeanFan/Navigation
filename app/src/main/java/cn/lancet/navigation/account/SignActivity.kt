package cn.lancet.navigation.account

import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.lancet.navigation.databinding.ActivitySignBinding
import cn.lancet.navigation.util.CommonUtil
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
                    AlertDialog.Builder(this@SignActivity).apply {
                        setTitle("温馨提示")
                        setNegativeButton("确认", object : OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }
                        })
                        setPositiveButton("取消", object : OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }

                        })
                    }.show()
                } else {
                    Toaster.showLong("注册失败，请稍后再试!")
                }
            }
        }
    }


}