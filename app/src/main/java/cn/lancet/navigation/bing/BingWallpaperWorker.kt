package cn.lancet.navigation.bing


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import okhttp3.OkHttpClient

/**
 * Bing 壁纸后台预加载任务。
 *
 * 职责：
 * 1. 后台请求今日 Bing 壁纸元数据；
 * 2. 保存本地缓存；
 * 3. 使用 Coil 预加载图片；
 * 4. HomeFragment 打开时可以更快显示。
 */
class BingWallpaperWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val repository = BingWallpaperRepository(
            context = applicationContext,
            okHttpClient = OkHttpClient()
        )

        val wallpaper = repository.refreshTodayWallpaperIfNeeded()
            ?: return Result.retry()

        return runCatching {
            val request = ImageRequest.Builder(applicationContext)
                .data(wallpaper.imageUrl)
                .build()

            val result = applicationContext.imageLoader.execute(request)

            if (result is SuccessResult) {
                Result.success()
            } else {
                Result.retry()
            }
        }.getOrElse {
            Result.retry()
        }
    }
}