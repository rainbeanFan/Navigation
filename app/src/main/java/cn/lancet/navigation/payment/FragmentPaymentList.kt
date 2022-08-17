package cn.lancet.navigation.payment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.databinding.FragmentPaymentListBinding
import cn.lancet.navigation.payment.adapter.PaymentAdapter
import cn.lancet.navigation.payment.data.PaymentData


class FragmentPaymentList : Fragment() {

    private val mPayments = mutableListOf<PaymentData>()

    private lateinit var mAdapter:PaymentAdapter

    private var mBinding: FragmentPaymentListBinding?=null
    private var mRvPayment:RecyclerView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPaymentListBinding.inflate(inflater,container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        mRvPayment = mBinding?.rvPayments
        mAdapter = PaymentAdapter(mPayments)
        mAdapter.setListener(object : PaymentAdapter.OnItemClickListener {
            override fun onItemClick(payment: PaymentData) {

                val action = FragmentPaymentListDirections.actionFragmentPaymentListToFragmentPaymentPay(
                    paymentId = payment.paymentId,
                    url = payment.url, title = payment.title, desc = payment.description, paid = payment.paid)

                val navigation = Navigation.findNavController(mBinding!!.root)
                navigation.navigate(action)
            }
        })
        mRvPayment?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = mAdapter
        }

        val bundle = FragmentPaymentPayArgs.fromBundle(requireArguments())

        val paid = bundle.paid
        val paymentId = bundle.paymentId

        mockData(paid,paymentId)
    }

    private fun mockData(paid: Boolean = false, paymentId: Int = -1) {
        for (index in 0 until 26){
            val paymentData = PaymentData(
                paymentId = index,
                title = "IPhone 1${index} Pro Max",
                url = "https://imgs.nmplus.hk/wp-content/uploads/2022/04/iphone-14-purple-image-outflow-1_16905228836267b5ebe9b39-1024x576.jpg.webp",
                description = "我是一条Payment描述"
            )
            mPayments.add(paymentData)
        }
        mAdapter.notifyDataSetChanged()
        if (paid){
            mPayments.forEachIndexed { index,payment ->
                if (payment.paymentId == paymentId){
                    val newPayment = payment.copy(paid = true)
                    mPayments[index] = newPayment
                    mAdapter.notifyItemInserted(index)
                    Handler().postDelayed({
                        requireActivity().runOnUiThread{
                            mRvPayment?.smoothScrollToPosition(index)
                        }
                    },300)

                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mRvPayment = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

}