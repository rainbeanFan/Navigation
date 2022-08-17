package cn.lancet.navigation.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.R
import cn.lancet.navigation.databinding.LayoutPaymentItemBinding
import cn.lancet.navigation.payment.data.PaymentData
import coil.load
import coil.transform.CircleCropTransformation

/**
 * Created by Lancet at 2022/8/17 13:02
 */
class PaymentAdapter(private val payments:MutableList<PaymentData>):RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    private var mListener:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PaymentViewHolder(LayoutPaymentItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = payments[position]

        holder.mView.ivGoodImage.load(payment.url){
            crossfade(true)
            placeholder(R.drawable.icon_shopping_basket)
            transformations(CircleCropTransformation())
        }
        holder.mView.tvGoodName.text = payment.title
        holder.mView.tvGoodDesc.text = payment.description

        holder.mView.btnPaymentStatus.text =
            if (payment.paid) "已支付" else "待支付"

        holder.mView.root.setOnClickListener {
            mListener?.onItemClick(payment)
        }

    }

    override fun getItemCount() = payments.size

    class PaymentViewHolder(itemView: LayoutPaymentItemBinding) : RecyclerView.ViewHolder(itemView.root){
        var mView:LayoutPaymentItemBinding
        init {
            mView = itemView
        }
    }

    fun setListener(listener: OnItemClickListener?){
        mListener = listener
    }

    interface OnItemClickListener{
        fun onItemClick(payment: PaymentData)
    }

}