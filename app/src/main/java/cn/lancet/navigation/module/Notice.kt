package cn.lancet.navigation.module

import cn.bmob.v3.BmobObject

data class Notice(
    var noticeId: String? = null,
    val createdTime: Long? = null,
    val updatedTime: Long? = null,
    val coverImageUrl: String? = null,
    val noticeTitle: String? = null,
    val noticeContent: String? = null,
    val publisher: String? = null,
    val publisherAvatar: String? = null,
    val likeCount: String? = null,
    val disCount: String? = null,
    val commentCount: String? = null,
    val shareCount: String? = null,
    val comment: Comment? = null

) : BmobObject() {
    override fun toString(): String {
        return "Notice(noticeId=$noticeId, createdTime=$createdTime, updatedTime=$updatedTime, coverImageUrl=$coverImageUrl, noticeTitle=$noticeTitle, noticeContent=$noticeContent, publisher=$publisher, publisherAvatar=$publisherAvatar, likeCount=$likeCount, disCount=$disCount, commentCount=$commentCount, shareCount=$shareCount, comment=$comment)"
    }
}

data class Comment(
    val commentId: String? = null,
    var noticeId: String? = null,
    var commentAvatarUrl: String? = null,
    var commentName: String? = null,
    val commentIsGod: Boolean? = null,
    val commentLikeCount: String? = null,
    val hasCommentLiked: Boolean? = null,
    var commentTitle: String? = null,
    var commentType: String? = null,
    var commentImageUrl: String? = null,
    val commentVideoUrl: String? = null
) : BmobObject() {
    override fun toString(): String {
        return "Comment(commentId=$commentId, noticeId=$noticeId, commentAvatarUrl=$commentAvatarUrl, commentName=$commentName, commentIsGod=$commentIsGod, commentLikeCount=$commentLikeCount, hasCommentLiked=$hasCommentLiked, commentTitle=$commentTitle, commentType=$commentType, commentImageUrl=$commentImageUrl, commentVideoUrl=$commentVideoUrl)"
    }
}
