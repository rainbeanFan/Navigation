package cn.lancet.navigation

import android.content.IntentSender
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commitNow
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
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
import com.hjq.toast.Toaster
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : AppCompatActivity() {

    private val updateRequestCode = 0x0001

    private var mBinding: ActivityMainBinding? = null

    private var mNavController: NavController? = null

    private val mAppUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.data == null) return@registerForActivityResult

        if (result.resultCode == updateRequestCode) {
            Toaster.show("开始更新...")
            if (result.resultCode != RESULT_OK) {
                Toaster.show("更新失败")
            }
        }
    }

    private val updateResultStarter =
        IntentSenderForResultStarter { intent, _, fillInIntent, flagsMask, flagsValues, _, _ ->
            val request = IntentSenderRequest.Builder(intent)
                .setFillInIntent(fillInIntent)
                .setFlags(flagsValues, flagsMask)
                .build()
            updateLauncher.launch(request)
        }

    private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
            val bytesDownloaded = state.bytesDownloaded()
            val totalBytesToDownload = state.totalBytesToDownload()
            Log.d("MainActivity", "Downloaded $bytesDownloaded / $totalBytesToDownload")
        }

        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            mBinding?.root?.let { root ->
                Snackbar.make(root, "New App is ready", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Restart") {
                        mAppUpdateManager.completeUpdate()
                    }
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupEdgeToEdgeWindow()

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(requireBinding().root)

        setupBottomGlassBar()
        setupBottomBarInsets()
        setupFab()

        mAppUpdateManager.registerListener(listener)

        checkUpdate()

        setupNavigation()
    }

    override fun onResume() {
        super.onResume()

        mAppUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (
                appUpdateInfo.updateAvailability() ==
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                mAppUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAppUpdateManager.unregisterListener(listener)
        mNavController = null
        mBinding = null
    }

    private fun setupEdgeToEdgeWindow() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F2F4F8")))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }

        val controller = WindowInsetsControllerCompat(window, window.decorView)

        // 首页背景整体偏亮，系统栏图标使用深色。
        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
    }

    private fun setupBottomGlassBar() {
        val binding = requireBinding()

        binding.bottomNavigationBlurView
            .setupWith(binding.mainRoot as ViewGroup, RenderScriptBlur(this))
            .setFrameClearDrawable(ColorDrawable(Color.TRANSPARENT))
            .setBlurRadius(20f)
            .setOverlayColor(Color.parseColor("#22FFFFFF"))

        binding.bottomNavigationContainer.bringToFront()
        binding.fabCreateNotice.bringToFront()
    }

    private fun setupBottomBarInsets() {
        val binding = requireBinding()

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainRoot) { _, insets ->
            val navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            binding.bottomNavigationContainer.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomMargin = navInsets.bottom + 12.dp()
            }

            insets
        }

        ViewCompat.requestApplyInsets(binding.mainRoot)
    }

    private fun setupFab() {
        val binding = requireBinding()

        binding.fabCreateNotice.setOnClickListener {
            Snackbar.make(
                binding.bottomNavigationContainer,
                "创建新的角色",
                Snackbar.LENGTH_SHORT
            )
                .setAction("好的") {
                    // no-op
                }
                .show()
        }
    }

    private fun setupNavigation() {
        val binding = requireBinding()

        val navHostFragment = NavHostFragment.create(R.navigation.lancet_navigation)

        supportFragmentManager.commitNow {
            replace(R.id.nav_host_fragment, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
        }

        mNavController = navHostFragment.navController

        mNavController?.navigatorProvider?.addNavigator(
            FixNavigator(
                this,
                supportFragmentManager,
                R.id.nav_host_fragment
            )
        )

        setupCustomBottomTabs()

        binding.tvHomeBadge.text = "99+"
        updateBottomTabSelected(isHomeSelected = true)
    }

    private fun setupCustomBottomTabs() {
        val binding = requireBinding()
        val navController = mNavController ?: return

        binding.tabHome.setOnClickListener {
            navigateToTopLevelDestination(R.id.fragmentA)
        }

        binding.tabMe.setOnClickListener {
            navigateToTopLevelDestination(R.id.fragmentC)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentA -> updateBottomTabSelected(isHomeSelected = true)
                R.id.fragmentC -> updateBottomTabSelected(isHomeSelected = false)
            }
        }
    }

    private fun navigateToTopLevelDestination(destinationId: Int) {
        val navController = mNavController ?: return

        if (navController.currentDestination?.id == destinationId) {
            updateBottomTabSelected(destinationId == R.id.fragmentA)
            return
        }

        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .setPopUpTo(
                navController.graph.startDestinationId,
                false,
                true
            )
            .build()

        runCatching {
            navController.navigate(destinationId, null, navOptions)
        }.onFailure { error ->
            Log.e("MainActivity", "navigate failed, destinationId=$destinationId", error)
        }
    }

    private fun updateBottomTabSelected(isHomeSelected: Boolean) {
        val binding = mBinding ?: return

        val selectedColor = Color.parseColor("#006D77")
        val normalColor = Color.parseColor("#7A7F83")

        binding.tabHomeContent.background =
            if (isHomeSelected) {
                ContextCompat.getDrawable(this, R.drawable.bg_bottom_tab_selected)
            } else {
                null
            }

        binding.tabMeContent.background =
            if (!isHomeSelected) {
                ContextCompat.getDrawable(this, R.drawable.bg_bottom_tab_selected)
            } else {
                null
            }

        binding.ivTabHome.setColorFilter(
            if (isHomeSelected) selectedColor else normalColor
        )
        binding.tvTabHome.setTextColor(
            if (isHomeSelected) selectedColor else normalColor
        )
        binding.tvTabHome.typeface =
            if (isHomeSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

        binding.ivTabMe.setColorFilter(
            if (!isHomeSelected) selectedColor else normalColor
        )
        binding.tvTabMe.setTextColor(
            if (!isHomeSelected) selectedColor else normalColor
        )
        binding.tvTabMe.typeface =
            if (!isHomeSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }

    private fun checkUpdate() {
        val appUpdateInfoTask = mAppUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateResultStarter,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                        updateRequestCode
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Toaster.show("更新失败")
                }
            }
        }
    }

    private fun requireBinding(): ActivityMainBinding {
        return mBinding ?: error("ActivityMainBinding is not initialized.")
    }

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density + 0.5f).toInt()
    }
}