package cn.lancet.navigation

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivitySplashBinding
import cn.lancet.navigation.util.AppPreUtils
import com.google.firebase.analytics.FirebaseAnalytics
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

        FirebaseAnalytics.getInstance(this)

//        mBinding!!.circleView.postDelayed({
//            mBinding!!.circleView.mUseFloatingLabel = false
//        },3000)

//        val animator = ObjectAnimator.ofFloat(mBinding!!.circleView, "radius", 150.dp)
//
//        animator.startDelay = 1500
//        animator.start()

        val intent = if (BmobUser.isLogin()) {
            Intent(this@SplashActivity, MainActivity::class.java)
        } else {
            Intent(this@SplashActivity, LoginActivity::class.java)
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