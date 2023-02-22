package cn.lancet.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.R
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.util.TimeUtil
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class NoticeAdapter(val context: Context) :
    RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private var mData = mutableListOf<Notice>()

    private var mListener: OnItemClickListener? = null

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

        holder.tvTime.text = data.createdTime?.let { TimeUtil.ctsToTimeStr(it) }
        holder.tvTitle.text = data.noticeTitle
        holder.tvContent.text = data.noticeContent
        holder.tvPublisher.text = data.publisher

        holder.llRoot.setOnClickListener {
            mListener?.onItemClick(data)
        }

    }

    fun setData(datas: MutableList<Notice>) {
        mData.clear()
        mData.addAll(datas)
        notifyDataSetChanged()
    }

    class NoticeViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llRoot: LinearLayout = itemView.findViewById(R.id.ll_root)
        val tvTime: AppCompatTextView = itemView.findViewById(R.id.tv_notification_time)
        val ivCover: ShapeableImageView = itemView.findViewById(R.id.iv_notification_image)
        val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tv_notification_title)
        val tvContent: AppCompatTextView = itemView.findViewById(R.id.tv_notification_content)
        val tvPublisher: AppCompatTextView = itemView.findViewById(R.id.tv_publisher)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(notice: Notice)
    }

}