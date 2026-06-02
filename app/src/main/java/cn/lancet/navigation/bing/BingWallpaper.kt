package cn.lancet.navigation.bing


/**
 * HomeFragment 每日 Bing 壁纸数据模型。
 *
 * @param startDate Bing 返回的图片日期，格式通常是 yyyyMMdd，例如 20260602
 * @param imageUrl 完整图片地址
 * @param copyright 图片版权信息，可用于后续展示
 */
data class BingWallpaper(
    val startDate: String,
    val imageUrl: String,
    val copyright: String
)
