package cn.lancet.navigation


data class AnimMessage(
    val giftType: String = "",
    val cfm: String? = null,
    val userName: String? = null,
    val giftDesc: Int = 0,
    val headUrl: String? = null,
    val userId: Int = 0,
    var giftNum: Int = 0,
    var isComboAnimationOver: Boolean = false,
    var updateTime: Long = 0,
    val couponName: String? = null,
    val giftName: String? = null,
    val giftTitle: String? = null,
)