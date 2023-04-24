package cn.lancet.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.R
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class HotAdapter(private val context: Context) : RecyclerView.Adapter<HotViewHolder>() {

    private var mData = mutableListOf<Pair<Int,String>>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_hot_item, parent, false)
        return HotViewHolder(view)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
        val data = mData[position]
        holder.ivAvatart.load(data.first)
        holder.tvName.text = data.second
    }

    fun setData(data:MutableList<Pair<Int,String>>){
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

}

class HotViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivAvatart = itemView.findViewById<ShapeableImageView>(R.id.iv_cover)
    val tvName = itemView.findViewById<AppCompatTextView>(R.id.tv_name)
}