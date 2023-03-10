package cn.lancet.navigation.net

data class PlantInfoRes(
    val log_id: Long,
    var result: MutableList<Result>?=null
) {
    override fun toString(): String {
        return "PlantInfoRes(log_id=$log_id, result=$result)"
    }
}

data class Result(
    var baike_info: BaikeInfo?=null,
    var name: String,
    var score: Double?=null,
    var probability: Double?=null,
    var calorie: Double?=null
) {
    override fun toString(): String {
        return "Result(baike_info=$baike_info, name='$name', score=$score, probability=$probability, calorie=$calorie)"
    }

}

data class BaikeInfo(
    val baike_url: String?=null,
    val image_url: String?=null,
    val description: String?=null
) {
    override fun toString(): String {
        return "BaikeInfo(baike_url='$baike_url', image_url='$image_url', description='$description')"
    }

}