package cn.lancet.navigation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.databinding.ActivitySplashBinding
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
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

        ImmersionBar.with(this).init()
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR)

        FirebaseAnalytics.getInstance(this)

        updateAppState()

        val intent = if (BmobUser.isLogin()){
            Intent(this@SplashActivity, MainActivity::class.java)
        }else{
            Intent(this@SplashActivity, LoginActivity::class.java)
        }

        mJob = flow {
            for (i in 5 downTo 1) {
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

    private fun updateAppState() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this, 0x001
                    )
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mJob?.cancel()
    }

}