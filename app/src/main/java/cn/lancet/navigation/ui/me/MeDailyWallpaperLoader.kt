package cn.lancet.navigation.ui.me


import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.launch

/**
 * MeFragment 每日壁纸加载器。
 *
 * 逻辑：
 * 1. 先显示本地缓存；
 * 2. 再根据今天日期生成今日图片 URL；
 * 3. 如果今日 URL 和缓存不同，自动切换；
 * 4. 图片加载失败时保持透明，不影响页面显示。
 */
class MeDailyWallpaperLoader(
    private val repository: MeDailyWallpaperRepository
) {

    fun loadInto(
        imageView: ImageView,
        lifecycleOwner: LifecycleOwner,
        alpha: Float = 1.0f
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

            val today = repository.getTodayWallpaper()

            if (today.imageUrl != cached?.imageUrl) {
                loadImage(
                    imageView = imageView,
                    imageUrl = today.imageUrl
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
}