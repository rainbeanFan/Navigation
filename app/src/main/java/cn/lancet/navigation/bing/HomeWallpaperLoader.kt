package cn.lancet.navigation.bing


import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.launch

/**
 * HomeFragment 壁纸加载器。
 *
 * 职责：
 * 1. HomeFragment 首屏优先显示本地缓存；
 * 2. 异步检查今日壁纸；
 * 3. 如果今日壁纸变化，自动淡入新图；
 * 4. 失败时无感兜底，不影响首页列表。
 */
class HomeWallpaperLoader(
    private val repository: BingWallpaperRepository
) {

    fun loadInto(
        imageView: ImageView,
        lifecycleOwner: LifecycleOwner,
        alpha: Float = DEFAULT_ALPHA
    ) {
        imageView.alpha = alpha

        lifecycleOwner.lifecycleScope.launch {
            val cached = repository.getCachedWallpaper()

            if (cached != null) {
                loadImage(
                    imageView = imageView,
                    imageUrl = cached.imageUrl
                )
            }

            val latest = repository.refreshTodayWallpaperIfNeeded()

            if (latest != null && latest.imageUrl != cached?.imageUrl) {
                loadImage(
                    imageView = imageView,
                    imageUrl = latest.imageUrl
                )
            }
        }
    }

    private fun loadImage(
        imageView: ImageView,
        imageUrl: String
    ) {
        imageView.load(imageUrl) {
            crossfade(true)
            placeholder(android.R.color.transparent)
            error(android.R.color.transparent)
        }
    }

    companion object {
        private const val DEFAULT_ALPHA = 0.30f
    }
}