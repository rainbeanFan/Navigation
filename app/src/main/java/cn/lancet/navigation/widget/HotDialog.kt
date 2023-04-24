package cn.lancet.navigation.widget

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import cn.lancet.navigation.R
import cn.lancet.navigation.adapter.HotAdapter
import cn.lancet.navigation.databinding.LayoutRightDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HotDialog():BottomSheetDialogFragment() {

    private var dialogRootView: FrameLayout? = null

    private lateinit var mBinding: LayoutRightDialogBinding

    private var mAdapter:HotAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundCornerBottomSheetDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.attributes?.gravity = Gravity.END
        dialogRootView = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        dialogRootView?.layoutParams?.width = (dialog?.window?.windowManager?.defaultDisplay!!.width * 0.8).toInt()
        dialogRootView?.layoutParams?.height = MATCH_PARENT
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = LayoutRightDialogBinding.inflate(inflater, container, false)
        initEvent()
        return mBinding.root
    }

    private fun initEvent() {
        mAdapter = HotAdapter(requireContext())
        mBinding.rvHot.layoutManager = GridLayoutManager(requireContext(),2)
        mBinding.rvHot.adapter = mAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mock()
    }

    private fun mock(){
        val mData = mutableListOf<Pair<Int,String>>()
        mData.clear()
        mData.add(Pair(R.drawable.avatar_1_raster,"麻花藤"))
        mData.add(Pair(R.drawable.avatar_2_raster,"丁三石"))
        mData.add(Pair(R.drawable.avatar_3_raster,"悔创阿里"))
        mData.add(Pair(R.drawable.avatar_4_raster,"风清气正"))
        mData.add(Pair(R.drawable.avatar_5_raster,"斯克🐴"))
        mData.add(Pair(R.drawable.avatar_6_raster,"尔泰若曦"))
        mData.add(Pair(R.drawable.avatar_7_raster,"Are you OK"))
        mData.add(Pair(R.drawable.avatar_8_raster,"只要我确定"))
        mAdapter?.setData(mData)
    }

}