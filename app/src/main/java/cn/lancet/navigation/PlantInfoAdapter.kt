package cn.lancet.navigation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.lancet.navigation.net.Result
import com.google.android.material.imageview.ShapeableImageView
import java.text.DecimalFormat
import coil.load


class PlantInfoAdapter(val context: Context) : RecyclerView.Adapter<PlantInfoAdapter.PlantInfoViewHolder>(){

    private var mData = mutableListOf<Result>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantInfoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_plant_info_item,parent,false)
        return PlantInfoViewHolder(view)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: PlantInfoViewHolder, position: Int) {
        val data = mData[position]

        holder.ivPlantPicture?.load(data.baike_info?.image_url){
            placeholder(R.mipmap.ic_notice_place_holder)
            error(R.mipmap.ic_notice_place_holder)
        }
        holder.tvPlantName?.text = data.name
        holder.tvPlantScore?.text = DecimalFormat("0.00%").format(data.score?:data.probability)
        holder.tvPlantCalorie?.visibility = if (data.calorie == null) View.GONE else View.VISIBLE
        holder.tvPlantCalorie?.text = "卡路里: "+data.calorie+"/100g"
        holder.tvPlantDescription?.text = data.baike_info?.description?:""
    }

    fun setData(data:MutableList<Result>){
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }
    inner class PlantInfoViewHolder(itemView:View):ViewHolder(itemView){
        var ivPlantPicture:ShapeableImageView? = itemView.findViewById(R.id.iv_plant_info)
        var tvPlantName:AppCompatTextView? = itemView.findViewById(R.id.tv_plant_name)
        var tvPlantScore:AppCompatTextView? = itemView.findViewById(R.id.tv_plant_score)
        var tvPlantCalorie:AppCompatTextView? = itemView.findViewById(R.id.tv_plant_calorie)
        var tvPlantDescription:AppCompatTextView? = itemView.findViewById(R.id.tv_plant_description)
    }



}