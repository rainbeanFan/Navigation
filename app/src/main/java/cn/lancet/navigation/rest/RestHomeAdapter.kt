package cn.lancet.navigation.rest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.lancet.navigation.R
import cn.lancet.navigation.module.RestType
import coil.load
import com.google.android.material.imageview.ShapeableImageView


class RestHomeAdapter(val context: Context) :
    RecyclerView.Adapter<RestHomeAdapter.RestHomeViewHolder>() {

    private var mData = mutableListOf<RestType>()

    private var mRestId = mutableListOf<Int>()

    private var mListener:OnRestTypeClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestHomeViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_rest_type_item, parent, false)
        return RestHomeViewHolder(view)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: RestHomeViewHolder, position: Int) {
        val data = mData[position]

        holder.ivRestType?.load(data.url) {
            placeholder(R.mipmap.icon_default_avatar)
            error(R.mipmap.icon_default_avatar)
        }
        holder.tvRestName?.text = data.name

        holder.itemView.setOnClickListener {
            mListener?.onItemClick(data)
        }

    }

    fun setData(data: MutableList<RestType>) {
        mData.clear()
        mRestId.clear()
        mData.addAll(data)
        data.forEach {
            mRestId.add(it.restId!!)
        }
        notifyDataSetChanged()
    }

    fun insertData(data:RestType){
        mData.add(0,data)
        mRestId.add(0,data.restId!!)
        notifyItemInserted(0)
    }

    fun deleteData(restId: Int){
        mData.removeAt(mRestId.indexOf(restId))
        notifyItemRemoved(mRestId.indexOf(restId))
    }

    inner class RestHomeViewHolder(itemView: View) : ViewHolder(itemView) {
        var ivRestType: ShapeableImageView? = itemView.findViewById(R.id.iv_rest)
        var tvRestName: AppCompatTextView? = itemView.findViewById(R.id.tv_rest_name)
        var tvHotType: AppCompatImageView? = itemView.findViewById(R.id.iv_hot)
    }

    fun setListener(listener:OnRestTypeClickListener):RestHomeAdapter{
        mListener = listener
        return this
    }

    interface OnRestTypeClickListener {
        fun onItemClick(restType: RestType)
    }

}