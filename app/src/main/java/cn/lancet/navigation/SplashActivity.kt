package cn.lancet.navigation

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivitySplashBinding
import cn.lancet.navigation.util.AppPreUtils
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
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

        updateAppState()

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

    private fun updateAppState(){
        val appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE){
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,0x001
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){

        }
    }

    val launchForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val intent = result.data
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        mJob?.cancel()
    }

}