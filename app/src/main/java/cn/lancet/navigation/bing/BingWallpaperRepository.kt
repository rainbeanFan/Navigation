package cn.lancet.navigation.bing


import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Bing 每日壁纸仓库。
 *
 * 职责：
 * 1. 请求 Bing 今日壁纸元数据；
 * 2. 解析完整图片 URL；
 * 3. 保存本地缓存；
 * 4. 控制每天最多主动请求一次，避免 HomeFragment 每次进入都打接口。
 */
class BingWallpaperRepository(
    context: Context,
    private val okHttpClient: OkHttpClient = OkHttpClient()
) {

    private val appContext = context.applicationContext

    private val prefs = appContext.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * 获取本地缓存壁纸。
     *
     * 这个方法不请求网络，适合 HomeFragment 首屏优先调用。
     */
    fun getCachedWallpaper(): BingWallpaper? {
        val startDate = prefs.getString(KEY_START_DATE, null).orEmpty()
        val imageUrl = prefs.getString(KEY_IMAGE_URL, null).orEmpty()
        val copyright = prefs.getString(KEY_COPYRIGHT, "").orEmpty()

        if (startDate.isBlank() || imageUrl.isBlank()) {
            return null
        }

        return BingWallpaper(
            startDate = startDate,
            imageUrl = imageUrl,
            copyright = copyright
        )
    }

    /**
     * 按天刷新 Bing 壁纸。
     *
     * 逻辑：
     * - 如果今天已经检查过，并且本地有缓存，直接返回缓存；
     * - 如果今天没检查过，请求 Bing 接口；
     * - 请求成功后更新本地缓存；
     * - 请求失败时返回旧缓存，避免首页空白。
     */
    suspend fun refreshTodayWallpaperIfNeeded(forceRefresh: Boolean = false): BingWallpaper? {
        val cached = getCachedWallpaper()
        val today = todayString()
        val lastCheckedDay = prefs.getString(KEY_LAST_CHECKED_DAY, null).orEmpty()

        if (!forceRefresh && cached != null && lastCheckedDay == today) {
            return cached
        }

        val remote = fetchTodayWallpaper()

        prefs.edit()
            .putString(KEY_LAST_CHECKED_DAY, today)
            .putLong(KEY_LAST_CHECKED_TIME, System.currentTimeMillis())
            .apply()

        if (remote == null) {
            return cached
        }

        saveWallpaper(remote)
        return remote
    }

    private fun saveWallpaper(wallpaper: BingWallpaper) {
        prefs.edit()
            .putString(KEY_START_DATE, wallpaper.startDate)
            .putString(KEY_IMAGE_URL, wallpaper.imageUrl)
            .putString(KEY_COPYRIGHT, wallpaper.copyright)
            .putLong(KEY_UPDATE_TIME, System.currentTimeMillis())
            .apply()
    }

    private suspend fun fetchTodayWallpaper(): BingWallpaper? {
        return withContext(Dispatchers.IO) {
            runCatching {
                val request = Request.Builder()
                    .url(BING_DAILY_API)
                    .get()
                    .build()

                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        return@withContext null
                    }

                    val body = response.body?.string().orEmpty()
                    if (body.isBlank()) {
                        return@withContext null
                    }

                    val root = JSONObject(body)
                    val images = root.optJSONArray("images") ?: return@withContext null
                    val first = images.optJSONObject(0) ?: return@withContext null

                    val startDate = first.optString("startdate").orEmpty()
                    val relativeUrl = first.optString("url").orEmpty()
                    val copyright = first.optString("copyright").orEmpty()

                    if (startDate.isBlank() || relativeUrl.isBlank()) {
                        return@withContext null
                    }

                    val imageUrl = buildBingImageUrl(relativeUrl)

                    BingWallpaper(
                        startDate = startDate,
                        imageUrl = imageUrl,
                        copyright = copyright
                    )
                }
            }.getOrNull()
        }
    }

    private fun buildBingImageUrl(url: String): String {
        return when {
            url.startsWith("http://") || url.startsWith("https://") -> url
            url.startsWith("/") -> "$BING_HOST$url"
            else -> "$BING_HOST/$url"
        }
    }

    private fun todayString(): String {
        return SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
    }

    companion object {
        private const val PREF_NAME = "home_bing_wallpaper_cache"

        private const val KEY_START_DATE = "start_date"
        private const val KEY_IMAGE_URL = "image_url"
        private const val KEY_COPYRIGHT = "copyright"
        private const val KEY_UPDATE_TIME = "update_time"
        private const val KEY_LAST_CHECKED_DAY = "last_checked_day"
        private const val KEY_LAST_CHECKED_TIME = "last_checked_time"

        private const val BING_HOST = "https://www.bing.com"

        private const val BING_DAILY_API =
            "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN&uhd=1"
    }
}