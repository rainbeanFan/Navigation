package cn.lancet.navigation.good

import android.app.PendingIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import cn.lancet.navigation.R
import com.google.android.material.button.MaterialButton


class FragmentGoodsList : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_goods_list, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        view.findViewById<AppCompatImageView>(R.id.iv_message).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.fragmentMessage)
        }

        view.findViewById<MaterialButton>(R.id.btn_pay).setOnClickListener{
            try {
                Navigation.findNavController(view).createDeepLink()
                    .setDestination(R.id.fragmentPaymentPay)
                    .createPendingIntent()
                    .send()
            }catch (e: PendingIntent.CanceledException) {

            }
        }

    }

}