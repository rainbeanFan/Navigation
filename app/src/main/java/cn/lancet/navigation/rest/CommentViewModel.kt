package cn.lancet.navigation.rest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.module.Comment
import cn.lancet.navigation.util.AppPreUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CommentViewModel:ViewModel() {

    var commentStateFlow = MutableSharedFlow<Boolean>()

    fun comment(noticeId:String,commentTitle:String,commentType:String){
        val comment = Comment()
        comment.noticeId = noticeId
        comment.commentAvatarUrl = AppPreUtils.getString(Constant.KEY_AVATAR)
        comment.commentName = AppPreUtils.getString(Constant.KEY_USER_NAME)
        comment.commentTitle = commentTitle
        comment.commentType = commentType
        comment.commentImageUrl = AppPreUtils.getString(Constant.KEY_AVATAR)

        comment.save(object : SaveListener<String>() {
            override fun done(commentId: String?, e: BmobException?) {
                viewModelScope.launch {
                    commentStateFlow.emit(e == null)
                }
            }
        })
    }

}