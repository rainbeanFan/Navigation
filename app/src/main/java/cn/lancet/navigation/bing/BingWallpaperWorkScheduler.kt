package cn.lancet.navigation.bing


import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Bing 每日壁纸 WorkManager 调度器。
 *
 * 建议在 Application.onCreate() 中调用：
 *
 * BingWallpaperWorkScheduler.enqueueDailyWork(this)
 * BingWallpaperWorkScheduler.enqueueOneTimeWarmUp(this)
 */
object BingWallpaperWorkScheduler {

    private const val UNIQUE_DAILY_WORK_NAME = "daily_bing_home_wallpaper_work"
    private const val UNIQUE_WARM_UP_WORK_NAME = "one_time_bing_home_wallpaper_warm_up"

    /**
     * 每天预加载一次 Bing 壁纸。
     *
     * 注意：
     * WorkManager 的周期任务不是精确定时器，
     * 系统会根据电量、网络、后台限制等条件安排执行。
     */
    fun enqueueDailyWork(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<BingWallpaperWorker>(
            24,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30,
                TimeUnit.SECONDS
            )
            .build()

        WorkManager.getInstance(context.applicationContext)
            .enqueueUniquePeriodicWork(
                UNIQUE_DAILY_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }

    /**
     * App 启动后的单次预热。
     *
     * 作用：
     * - 新用户第一次打开 App 时，尽快准备好首页背景；
     * - 老用户如果周期任务暂时没跑，也能补一次。
     */
    fun enqueueOneTimeWarmUp(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<BingWallpaperWorker>()
            .setConstraints(constraints)
            .setInitialDelay(3, TimeUnit.SECONDS)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30,
                TimeUnit.SECONDS
            )
            .build()

        WorkManager.getInstance(context.applicationContext)
            .enqueueUniqueWork(
                UNIQUE_WARM_UP_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}