package cn.lancet.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.R
import cn.lancet.navigation.module.User
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class ContactAdapter(val context: Context):RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var mData = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_user_item,parent,false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        val data = mData[position]

        holder.ivAvatar.load(data.avatar){
            placeholder(R.mipmap.icon_default_avatar)
            error(R.mipmap.icon_default_avatar)
        }

        holder.tvName.text = data.name
        holder.tvEmail.text = data.email
    }

    fun setData(datas: MutableList<User>) {
        mData.clear()
        mData.addAll(datas)
        notifyDataSetChanged()
    }

    class ContactViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivAvatar: ShapeableImageView = itemView.findViewById(R.id.iv_avatar)
        val tvName: AppCompatTextView = itemView.findViewById(R.id.tv_name)
        val tvEmail: AppCompatTextView = itemView.findViewById(R.id.tv_email)
    }

}