package cn.lancet.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.R
import cn.lancet.navigation.module.Comment
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.util.TimeUtil
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView

class NoticeAdapter(val context: Context) :
    RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private var mData = mutableListOf<Notice>()

    private var mListener: OnItemClickListener? = null
    private var mCommentListener: OnCommentClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_note_item, parent, false)
        return NoticeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {

        val data = mData[position]

        holder.ivCover.load(data.coverImageUrl) {
            placeholder(R.mipmap.ic_notice_place_holder)
            error(R.mipmap.ic_notice_place_holder)
        }

        holder.ivPublisher.load(data.publisherAvatar) {
            placeholder(R.mipmap.ic_notice_place_holder)
            error(R.mipmap.ic_notice_place_holder)
        }

        holder.tvTime.text = data.updatedAt
        holder.tvTitle.text = data.noticeTitle
        holder.tvContent.text = data.noticeContent
        holder.tvPublisher.text = data.publisher

        holder.tvLike.text = data.likeCount
        holder.tvDis.text = data.disCount
        holder.tvComment.text = data.commentCount
        holder.tvShare.text = data.shareCount

        holder.llRoot.setOnClickListener {
            mListener?.onItemClick(data)
        }

        holder.tvComment.setOnClickListener {
            mCommentListener?.onCommentClick(data)
        }

    }

    override fun onBindViewHolder(
        holder: NoticeViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        val holder = holder

        val avatarUrl =
            "https://images.pexels.com/photos/2174974/pexels-photo-2174974.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"

        payloads.forEach {
            if (it is Comment){
                holder.ivCommentAvatar.load(it.commentAvatarUrl){

                }
                holder.tvCommentName.text = it.commentName
                holder.tvCommentTitle.text = it.commentTitle
                holder.ivCommentCover.load(avatarUrl)
            }
        }
    }

    fun setData(datas: MutableList<Notice>) {
        mData.clear()
        mData.addAll(datas)
        notifyDataSetChanged()
    }

    fun addData(data: Notice) {
        mData.add(0,data)
        notifyItemInserted(0)
    }

    class NoticeViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llRoot: LinearLayout = itemView.findViewById(R.id.ll_root)
        val tvTime: AppCompatTextView = itemView.findViewById(R.id.tv_notification_time)
        val ivCover: ShapeableImageView = itemView.findViewById(R.id.iv_notification_image)
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tv_notification_title)
        val tvContent: AppCompatTextView = itemView.findViewById(R.id.tv_notification_content)
        val tvPublisher: AppCompatTextView = itemView.findViewById(R.id.tv_publisher)
        val ivPublisher: ShapeableImageView = itemView.findViewById(R.id.iv_publisher_avatar)

        val tvLike:MaterialButton = itemView.findViewById(R.id.like)
        val tvDis:MaterialButton = itemView.findViewById(R.id.dis)
        val tvComment:MaterialButton = itemView.findViewById(R.id.comment)
        val tvShare:MaterialButton = itemView.findViewById(R.id.share)

        val commentLayout: MaterialCardView = itemView.findViewById(R.id.card_layout)
        val ivCommentAvatar:ShapeableImageView = itemView.findViewById(R.id.comment_avatar)

        val tvCommentName:AppCompatTextView = itemView.findViewById(R.id.comment_name)
        val ivCommentLike:AppCompatImageView = itemView.findViewById(R.id.comment_like)

        val tvCommentLikeCount:AppCompatTextView = itemView.findViewById(R.id.tv_comment_like_count)
        val ivGodComment:AppCompatImageView = itemView.findViewById(R.id.iv_god_comment)

        val tvCommentTitle:AppCompatTextView = itemView.findViewById(R.id.tv_comment_title)
        val tvCommentLayout:FrameLayout = itemView.findViewById(R.id.fl_comment_image)

        val ivCommentCover:ShapeableImageView = itemView.findViewById(R.id.iv_comment_cover)
        val ivCommentPlay:AppCompatImageView = itemView.findViewById(R.id.iv_play)


    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    fun setOnCommentClickListener(listener: OnCommentClickListener) {
        mCommentListener = listener
    }

    interface OnCommentClickListener {
        fun onCommentClick(notice: Notice)
    }

    interface OnItemClickListener {
        fun onItemClick(notice: Notice)
    }

}