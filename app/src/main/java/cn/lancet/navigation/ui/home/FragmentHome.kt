package cn.lancet.navigation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.adapter.NoticeAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.FragmentHomeBinding
import cn.lancet.navigation.module.PlantDiscoveryInfo
import cn.lancet.navigation.module.User
import cn.lancet.navigation.rest.NoticeDetailActivity
import cn.lancet.navigation.rest.NoticeViewModel
import com.hjq.toast.Toaster
import kotlinx.coroutines.launch
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

    private lateinit var viewModel: PlantListViewModel
    private lateinit var viewModelNotice: NoticeViewModel

    private var mNoticeIds = mutableListOf<String>()

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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[PlantListViewModel::class.java]

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
            override fun onItemClick(plant: PlantDiscoveryInfo) {
                val intent = Intent(requireContext(), NoticeDetailActivity::class.java)
                intent.putExtra(Constant.KEY_REST_ID, plant.objectId)
                startActivity(intent)
            }

        })

        getNotices()

        lifecycleScope.launch {
            viewModel.mPlantInfoFlow.collect {
                mAdapter?.setData(it)
                it.forEach { notice ->
                    mNoticeIds.add(notice.objectId)
                }
            }
        }
    }

    private fun getNotices() {
        if (BmobUser.isLogin()){
            viewModel.getPlantInfo(BmobUser.getCurrentUser(User::class.java).objectId)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String) {
        when (event) {
            "deleteNotice" -> getNotices()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mRvMessage = null
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}