package cn.lancet.navigation.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.adapter.NoticeAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.FragmentHomeBinding
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.notice.NoticeDetailActivity
import cn.lancet.navigation.notice.NoticeViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FragmentHome : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private var mRvMessage: RecyclerView? = null

    private var mAdapter: NoticeAdapter? = null

    private lateinit var viewModel: NoticeListViewModel
    private lateinit var viewModelNotice: NoticeViewModel

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[NoticeListViewModel::class.java]

        viewModelNotice = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[NoticeViewModel::class.java]

        mAdapter = NoticeAdapter(requireContext())
        mRvMessage = binding.rvMessage
        mRvMessage?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
        mAdapter?.setOnItemClickListener(object : NoticeAdapter.OnItemClickListener {
            override fun onItemClick(notice: Notice) {
                val intent = Intent(requireContext(), NoticeDetailActivity::class.java)
                intent.putExtra(Constant.KEY_NOTICE_ID, notice.objectId)
                startActivity(intent)
            }
        })

        getNotices()

        viewModel._notices.observe(viewLifecycleOwner, Observer {
            mAdapter?.setData(it)
        })

        viewModelNotice.addNotice.observe(viewLifecycleOwner){
            mAdapter?.addData(it)
        }

    }

    private fun getNotices() {
        viewModel.getNoticeList()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String) {
        when (event) {
            "addNotice" -> getNotices()
            "deleteNotice" -> getNotices()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        mRvMessage = null

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }

        Log.d("onDestroy  ", "Fragment A")

    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyView  ", "Fragment A")

        _binding = null
    }


}