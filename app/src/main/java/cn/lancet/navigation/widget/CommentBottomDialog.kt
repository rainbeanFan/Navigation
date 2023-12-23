package cn.lancet.navigation.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.lancet.navigation.R
import cn.lancet.navigation.rest.CommentViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.hjq.toast.Toaster
import kotlinx.coroutines.launch

class CommentBottomDialog(noticeId:String):BottomSheetDialogFragment() {

    private var dialogRootView: FrameLayout? = null
    private var mNoticeId:String?=null

    private lateinit var viewModel: CommentViewModel
    init {
        val bundle = Bundle()
        bundle.putString("NOTICE_ID",noticeId)
        arguments = bundle
    }

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
        mNoticeId = arguments?.getString("NOTICE_ID")?:""

        val dialog =  super.onCreateDialog(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_remark_dialog,null)
        dialog.setContentView(view)
        initView(view)
        return dialog
    }

    private fun initView(view: View){

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[CommentViewModel::class.java]

        view.findViewById<AppCompatImageView>(R.id.iv_close_remark).setOnClickListener { dismissAllowingStateLoss()}

        val etCommentContent = view.findViewById<AppCompatEditText>(R.id.et_remark)

        view.findViewById<MaterialButton>(R.id.btn_commit).setOnClickListener {
//            val commentContent = etCommentContent.text.toString()
//            mNoticeId?.let{
//                viewModel.comment(it,commentContent,"Image")
//            }
        }

        lifecycleScope.launch {
            viewModel.commentStateFlow.collect {
                if (it){
                    Toaster.showLong("评论成功啦！")
                    dismissAllowingStateLoss()
                }else{
                    Toaster.showLong("评论失败啦！")
                    dismissAllowingStateLoss()
                }
            }
        }


    }



















}