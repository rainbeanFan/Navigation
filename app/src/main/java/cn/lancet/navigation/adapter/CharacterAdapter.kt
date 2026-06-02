package cn.lancet.navigation.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.R
import cn.lancet.navigation.module.Character
import coil.load
import coil.transform.BlurTransformation
import com.google.android.material.imageview.ShapeableImageView

class CharacterAdapter(private val context: Context) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private val mData = mutableListOf<Character>()

    private var mListener: OnItemClickListener? = null

    /**
     * 使用固定渐变色池，而不是完全随机。
     *
     * 原因：
     * RecyclerView 会复用 item，如果每次 onBind 都 Random，
     * 滑动时颜色会不停变化，看起来像 UI 抽风。
     *
     * 用 position % size 可以做到：
     * 1. 每个卡片颜色不同；
     * 2. 滑动复用时颜色稳定；
     * 3. 整体仍然有“随机渐变”的视觉效果。
     */
    private val gradientColorPool = listOf(
        intArrayOf(Color.parseColor("#7C4DFF"), Color.parseColor("#536DFE"), Color.parseColor("#00BCD4")),
        intArrayOf(Color.parseColor("#FF6A88"), Color.parseColor("#FF99AC"), Color.parseColor("#FFC371")),
        intArrayOf(Color.parseColor("#11998E"), Color.parseColor("#38EF7D"), Color.parseColor("#A8FF78")),
        intArrayOf(Color.parseColor("#FC466B"), Color.parseColor("#3F5EFB"), Color.parseColor("#8E2DE2")),
        intArrayOf(Color.parseColor("#F7971E"), Color.parseColor("#FFD200"), Color.parseColor("#FF512F")),
        intArrayOf(Color.parseColor("#00C6FF"), Color.parseColor("#0072FF"), Color.parseColor("#6A11CB")),
        intArrayOf(Color.parseColor("#DA22FF"), Color.parseColor("#9733EE"), Color.parseColor("#4776E6")),
        intArrayOf(Color.parseColor("#F953C6"), Color.parseColor("#B91D73"), Color.parseColor("#6A0572")),
        intArrayOf(Color.parseColor("#36D1DC"), Color.parseColor("#5B86E5"), Color.parseColor("#7F7FD5")),
        intArrayOf(Color.parseColor("#FF9966"), Color.parseColor("#FF5E62"), Color.parseColor("#C33764"))
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_character_item, parent, false)
        return CharacterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val data = mData[position]

        holder.cardContent.applyGradientBackground(position)

        holder.ivCharacterAvatar.load(data.avatar) {
            placeholder(R.mipmap.icon_default_avatar)
            error(R.mipmap.icon_default_avatar)
            transformations(BlurTransformation(context, 5f, 10f))
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
        mData.add(0, data)
        notifyItemInserted(0)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    private fun View.applyGradientBackground(position: Int) {
        val colors = gradientColorPool[position % gradientColorPool.size]

        background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            colors
        ).apply {
            cornerRadius = 22f.dp()
        }
    }

    private fun Float.dp(): Float {
        return this * context.resources.displayMetrics.density
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * root 现在建议是 MaterialCardView。
         * 这里用 View 接收，避免你以后从 ConstraintLayout 换成 MaterialCardView 后类型崩掉。
         */
        val root: View = itemView.findViewById(R.id.root)

        /**
         * 这个 id 来自前面优化后的 layout_character_item.xml：
         *
         * android:id="@+id/cl_character_card_content"
         *
         * 外层 MaterialCardView 负责阴影，
         * 这个内层容器负责动态渐变背景。
         */
        val cardContent: View = itemView.findViewById(R.id.cl_character_card_content)

        val tvPromptTitle: AppCompatTextView = itemView.findViewById(R.id.tv_prompt_title)
        val tvCharacterUserNum: AppCompatTextView = itemView.findViewById(R.id.tv_character_user_num)
        val tvCharacterDescription: AppCompatTextView =
            itemView.findViewById(R.id.tv_character_description)
        val ivCharacterAvatar: ShapeableImageView = itemView.findViewById(R.id.iv_character_avatar)
    }

    interface OnItemClickListener {
        fun onItemClick(character: Character)
    }
}