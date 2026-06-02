package cn.lancet.navigation.ui.me


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.imageLoader
import coil.request.ImageRequest

/**
 * MeFragment 每日壁纸后台预加载。
 *
 * 作用：
 * - App 后台提前把今日 Picsum 图片放进 Coil 缓存；
 * - 进入 MeFragment 时更快显示；
 * - 失败时重试，不影响主流程。
 */
class MeDailyWallpaperWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return runCatching {
            val repository = MeDailyWallpaperRepository(applicationContext)
            val wallpaper = repository.getTodayWallpaper()

            val request = ImageRequest.Builder(applicationContext)
                .data(wallpaper.imageUrl)
                .build()

            applicationContext.imageLoader.execute(request)

            Result.success()
        }.getOrElse {
            Result.retry()
        }
    }
}