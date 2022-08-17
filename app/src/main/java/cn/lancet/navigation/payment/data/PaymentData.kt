package cn.lancet.navigation.payment.data

/**
 * Created by Lancet at 2022/8/17 13:01
 */
data class PaymentData(val paymentId:Int,val url:String,val title:String,val description:String,val paid:Boolean = false)
