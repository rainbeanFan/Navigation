package cn.lancet.navigation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivitySplashBinding
import cn.lancet.navigation.util.AppPreUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class SplashActivity : AppCompatActivity() {

    private var mJob: Job? = null

    private var mBinding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        val intent = if (AppPreUtils.getString(Constant.KEY_USER_ID).isBlank()) {
            Intent(this@SplashActivity, LoginActivity::class.java)
        } else {
            Intent(this@SplashActivity, MainActivity::class.java)
        }

        mJob = flow {
            for (i in 5 downTo  1) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.Main)
            .onEach {
                mBinding?.tvJump?.text = "跳过(${it}s)"
            }
            .onCompletion {
                startActivity(intent)
                finish()
            }.launchIn(lifecycleScope)

        mBinding?.tvJump?.setOnClickListener {
            startActivity(intent)
            finish()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        mJob?.cancel()
    }

}