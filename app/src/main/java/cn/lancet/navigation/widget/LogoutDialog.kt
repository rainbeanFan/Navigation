package cn.lancet.navigation.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import cn.lancet.navigation.R

class LogoutDialog : DialogFragment(){

    private var mListener: OnLogoutClickListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.RoundCornerBottomSheetDialog)
    }

    override fun onStart() {
        super.onStart()

        val window = dialog?.window
        if (window != null) {
            val dm = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(dm)
            window.setLayout(
                (dm.widthPixels * 0.75).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            val params = window.attributes
            params.gravity = Gravity.CENTER
            window.apply {
                attributes = params
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_logout_dialog, container)
        dialog?.setCancelable(true)
        view.findViewById<AppCompatTextView>(R.id.tv_cancel).setOnClickListener { dismissAllowingStateLoss()}
        view.findViewById<AppCompatTextView>(R.id.tv_sure).setOnClickListener {
            mListener?.logout()
            dismissAllowingStateLoss()
        }
        return view
    }

    fun setOnLogoutListener(listener: OnLogoutClickListener):LogoutDialog{
        mListener = listener
        return this
    }

    interface OnLogoutClickListener {
        fun logout()
    }

    companion object {
        fun newInstance() = LogoutDialog()
    }

}