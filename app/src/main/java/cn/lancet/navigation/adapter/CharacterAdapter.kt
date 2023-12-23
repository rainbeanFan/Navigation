package cn.lancet.navigation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.R
import cn.lancet.navigation.module.Character
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class CharacterAdapter(val context: Context) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var mData = mutableListOf<Character>()

    private var mListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_character_item, parent, false)
        return CharacterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {

        val data = mData[position]
        holder.ivCharacterAvatar.load(data.avatar) {
            placeholder(R.mipmap.icon_default_avatar)
            error(R.mipmap.icon_default_avatar)
        }

        holder.tvPromptTitle.text = data.prompt
        holder.tvCharacterUserNum.text = data.userNum
        holder.tvCharacterDescription.text = data.description
        holder.root.setOnClickListener {
            mListener?.onItemClick(data)
        }
    }

    fun setData(datas: MutableList<Character>) {
        mData.clear()
        mData.addAll(datas)
        notifyDataSetChanged()
    }

    fun addData(data: Character) {
        mData.add(0,data)
        notifyItemInserted(0)
    }

    class CharacterViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root:ConstraintLayout = itemView.findViewById(R.id.root)
        val tvPromptTitle: AppCompatTextView = itemView.findViewById(R.id.tv_prompt_title)
        val tvCharacterUserNum: AppCompatTextView = itemView.findViewById(R.id.tv_character_user_num)
        val tvCharacterDescription: AppCompatTextView = itemView.findViewById(R.id.tv_character_description)
        val ivCharacterAvatar: ShapeableImageView = itemView.findViewById(R.id.iv_character_avatar)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(character: Character)
    }

}