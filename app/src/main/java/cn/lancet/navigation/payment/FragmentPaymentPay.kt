package cn.lancet.navigation.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cn.lancet.navigation.R
import cn.lancet.navigation.databinding.FragmentPaymentPayBinding
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView


class FragmentPaymentPay : Fragment() {

    private var mBinding:FragmentPaymentPayBinding?=null
    private var mImage: ShapeableImageView?=null
    private var mTitle:TextView?=null
    private var mDesc: TextView?=null
    private var mPayment: MaterialButton?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentPaymentPayBinding.inflate(inflater,container,false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView() {
        mImage = mBinding?.ivPaymentImage
        mTitle = mBinding?.tvPaymentTitle
        mDesc = mBinding?.tvPaymentDesc
        mPayment = mBinding?.btnPayment

        val bundle = FragmentPaymentListArgs.fromBundle(requireArguments())

        val url = bundle.url
        val title = bundle.title
        val desc = bundle.desc
        val paymentId = bundle.paymentId

        mImage?.load(url){
            crossfade(true)
            placeholder(R.drawable.icon_shopping_basket)
            transformations(CircleCropTransformation())
        }

        mTitle?.text = title
        mDesc?.text = desc

        mPayment?.setOnClickListener{
            val action = FragmentPaymentPayDirections.actionFragmentPaymentPayToFragmentPaymentResult(
                paymentId = paymentId,paid = true
            )
            val bundle = action.arguments
            Navigation.findNavController(mBinding!!.root).setGraph(R.navigation.payment_navigation,bundle)
            Navigation.findNavController(mBinding!!.root).popBackStack()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        mTitle = null
        mDesc = null
        mPayment = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}