package cn.lancet.navigation


import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cn.lancet.navigation.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.Toaster

class MainActivity : AppCompatActivity() {

    val UPDATE_REQUEST_CODE = 0x0001

    private var mBinding: ActivityMainBinding? = null

    private var mNavController: NavController? = null

    private val mAppUpdateManager:AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.data == null) return@registerForActivityResult
        if (result.resultCode == UPDATE_REQUEST_CODE) {
            Toaster.show("开始更新...")
            if (result.resultCode != RESULT_OK) {
                Toaster.show("更新失败")
            }
        }
    }

    private val updateResultStarter =
        IntentSenderForResultStarter { intent,_,fillInIntent,flagsMask,flagsValues,_,_ ->
            val request = IntentSenderRequest.Builder(intent)
                .setFillInIntent(fillInIntent)
                .setFlags(flagsValues, flagsMask)
                .build()
            updateLauncher.launch(request)
        }

    val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
            val bytesDownloaded = state.bytesDownloaded()
            val totalBytesToDownload = state.totalBytesToDownload()
            Log.d("MainActivity", "Downloaded $bytesDownloaded / $totalBytesToDownload")
        }
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Snackbar.make(mBinding!!.root, "New App is ready", Snackbar.LENGTH_INDEFINITE)
                .setAction("Restart") {
                    mAppUpdateManager.completeUpdate()
                }
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        ImmersionBar.with(this).init()
        ImmersionBar.with(this).statusBarDarkFont(true).init()

        mAppUpdateManager.registerListener(listener)

        checkUpdate()

        mBinding?.fabCreateNotice?.setOnClickListener {
            Snackbar.make(mBinding!!.bottomNavigationView,"创建新的角色",Snackbar.LENGTH_SHORT)
                .setAction("好的"){

                }.show()
        }

        val navHostFragment = NavHostFragment.create(R.navigation.lancet_navigation)

        supportFragmentManager.commitNow {
            this.replace(R.id.nav_host_fragment, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
        }


        mNavController = navHostFragment.navController

        mNavController?.navigatorProvider?.addNavigator(
            FixNavigator(this, supportFragmentManager, R.id.nav_host_fragment)
        )

        NavigationUI.setupWithNavController(mBinding!!.bottomNavigationView, mNavController!!)

    }

    override fun onResume() {
        super.onResume()
        mAppUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                mAppUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAppUpdateManager.unregisterListener(listener)
    }

    private fun checkUpdate(){
        val appUpdateInfoTask = mAppUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateResultStarter,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                        UPDATE_REQUEST_CODE
                    )
                }catch (e:IntentSender.SendIntentException){
                    Toaster.show("更新失败")
                }
            }
        }

    }

}