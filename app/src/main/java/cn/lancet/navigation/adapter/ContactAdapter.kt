package cn.lancet.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SectionIndexer
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.R
import cn.lancet.navigation.module.User
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class ContactAdapter(val context: Context) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(),
    SectionIndexer {

    private var mData = mutableListOf<User>()

    private var mListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_user_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        val data = mData[position]

        holder.ivAvatar.load(data.avatar) {
            placeholder(R.mipmap.icon_default_avatar)
            error(R.mipmap.icon_default_avatar)
        }

        holder.tvName.text = data.name
        holder.tvEmail.text = data.email

        val section = getSectionForPosition(position)
        if (position == getPositionForSection(section)) {
            holder.tvCatalog.visibility = View.VISIBLE
            holder.tvCatalog.text = mData[position].sort_letter
        } else {
            holder.tvCatalog.visibility = View.GONE
        }

        holder.llContactLayout.setOnClickListener {
            mListener?.onItemClick(data)
        }

    }

    fun setData(datas: MutableList<User>) {
        mData.clear()
        mData.addAll(datas)
        notifyDataSetChanged()
    }

    class ContactViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llContactLayout: LinearLayout = itemView.findViewById(R.id.contact_layout)
        val tvCatalog: AppCompatTextView = itemView.findViewById(R.id.tv_catalog)
        val ivAvatar: ShapeableImageView = itemView.findViewById(R.id.iv_avatar)
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tv_name)
        val tvEmail: AppCompatTextView = itemView.findViewById(R.id.tv_email)
    }

    override fun getSections(): Array<Any>? {
        return null
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        for (i in 0 until mData.size) {
            val firstChar = mData[i].sort_letter?.get(0)?.code
            if (firstChar == sectionIndex) {
                return i
            }
        }
        return -1
    }

    override fun getSectionForPosition(position: Int): Int {
        return mData[position].sort_letter?.get(0)?.code ?: 0
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(user: User)
    }

}