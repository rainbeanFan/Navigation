package cn.lancet.navigation.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import cn.lancet.navigation.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class SelectPictureBottomDialog() : BottomSheetDialogFragment() {

    private var dialogRootView: FrameLayout? = null

    private var mListener:OnItemClickListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundCornerBottomSheetDialog)
    }

    override fun onStart() {
        super.onStart()

        dialogRootView = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        dialogRootView?.layoutParams?.height = WRAP_CONTENT
        dialogRootView?.let { view ->
            BottomSheetBehavior.from(view).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_select_picture_dialog, null)
        dialog.setContentView(view)
        initView(view)
        return dialog
    }

    private fun initView(view: View) {

        view.findViewById<MaterialButton>(R.id.btn_camera).setOnClickListener {
            mListener?.openCamera()
            dialog?.dismiss()
        }

        view.findViewById<MaterialButton>(R.id.btn_gallery).setOnClickListener {
            mListener?.openGallery()
            dialog?.dismiss()
        }

        view.findViewById<MaterialButton>(R.id.btn_cancel).setOnClickListener {
            dialog?.dismiss()
        }

    }

    fun setListener(listener: OnItemClickListener):SelectPictureBottomDialog{
        mListener = listener
        return this
    }

    interface OnItemClickListener {
        fun openCamera(){}

        fun openGallery(){}
    }

}