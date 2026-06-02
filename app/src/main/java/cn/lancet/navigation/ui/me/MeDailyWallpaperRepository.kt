package cn.lancet.navigation.ui.me


import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * MeFragment 每日动态壁纸仓库。
 *
 * 数据源：
 * - 不再使用 Bing。
 * - 使用 Lorem Picsum seed 图片。
 *
 * 规则：
 * - 同一天 seed 固定，图片固定；
 * - 第二天 day 变化，seed 变化，图片自动变化；
 * - 不需要请求 JSON，不需要 API Key；
 * - Coil 会按 URL 做缓存。
 */
class MeDailyWallpaperRepository(
    context: Context
) {

    private val appContext = context.applicationContext

    private val prefs = appContext.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    fun getCachedWallpaper(): MeDailyWallpaper? {
        val day = prefs.getString(KEY_DAY, null).orEmpty()
        val imageUrl = prefs.getString(KEY_IMAGE_URL, null).orEmpty()
        val seed = prefs.getString(KEY_SEED, null).orEmpty()

        if (day.isBlank() || imageUrl.isBlank() || seed.isBlank()) {
            return null
        }

        return MeDailyWallpaper(
            day = day,
            imageUrl = imageUrl,
            seed = seed
        )
    }

    fun getTodayWallpaper(): MeDailyWallpaper {
        val today = todayString()
        val cached = getCachedWallpaper()

        if (cached != null && cached.day == today) {
            return cached
        }

        val seed = "walden_me_$today"
        val imageUrl = buildImageUrl(seed)

        val wallpaper = MeDailyWallpaper(
            day = today,
            imageUrl = imageUrl,
            seed = seed
        )

        prefs.edit()
            .putString(KEY_DAY, wallpaper.day)
            .putString(KEY_IMAGE_URL, wallpaper.imageUrl)
            .putString(KEY_SEED, wallpaper.seed)
            .putLong(KEY_UPDATE_TIME, System.currentTimeMillis())
            .apply()

        return wallpaper
    }

    private fun buildImageUrl(seed: String): String {
        return "$PICSUM_HOST/seed/$seed/$IMAGE_WIDTH/$IMAGE_HEIGHT"
    }

    private fun todayString(): String {
        return SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
    }

    companion object {
        private const val PREF_NAME = "me_daily_wallpaper_cache"

        private const val KEY_DAY = "day"
        private const val KEY_IMAGE_URL = "image_url"
        private const val KEY_SEED = "seed"
        private const val KEY_UPDATE_TIME = "update_time"

        private const val PICSUM_HOST = "https://picsum.photos"

        private const val IMAGE_WIDTH = 1080
        private const val IMAGE_HEIGHT = 1920
    }
}