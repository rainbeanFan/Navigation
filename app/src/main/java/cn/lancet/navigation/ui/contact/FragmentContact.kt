package cn.lancet.navigation.ui.contact


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.adapter.ContactAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.FragmentContactBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.AppPreUtils
import cn.lancet.navigation.util.FirstLetterComparator
import cn.lancet.navigation.widget.SideBar
import com.hjq.toast.Toaster
import io.rong.imkit.IMCenter
import io.rong.imkit.utils.RouteUtils
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.message.TextMessage
import kotlinx.coroutines.launch


class FragmentContact : Fragment() {

    private var _binding: FragmentContactBinding? = null

    private var mRvContact: RecyclerView? = null

    private var mHitLetter: AppCompatTextView? = null

    private var mSideBar: SideBar? = null

    private var mAdapter: ContactAdapter? = null

    private var mComparator = FirstLetterComparator()

    private lateinit var viewModel: ContactListViewModel

    private val binding get() = _binding!!

    private var mLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRvContact = binding.rvContact
        mHitLetter = binding.tvHintLetter
        mSideBar = binding.sideBar

        mAdapter = ContactAdapter(requireContext())

        initData()

    }

    private fun initData() {
        mRvContact?.layoutManager = LinearLayoutManager(requireContext())

        mRvContact?.adapter = mAdapter

        mSideBar?.setTextView(mHitLetter)
        mSideBar?.setOnTouchingLetterChangedListener(object :
            SideBar.OnTouchingLetterChangedListener {
            override fun onTouchingLetterChanged(letter: String?) {
                val position = letter?.let { mAdapter?.getPositionForSection(it.codePointAt(0)) }
                if (position != null && position != -1) {
                    (mRvContact?.layoutManager as LinearLayoutManager).scrollToPosition(position)
                }
            }
        })

        mAdapter?.setOnItemClickListener(object : ContactAdapter.OnItemClickListener {
            override fun onItemClick(user: User) {

                RouteUtils.routeToConversationActivity(
                    requireContext(),
                    Conversation.ConversationType.PRIVATE,
                    user.objectId
                )

//                val message = Message.obtain(user.objectId,
//                    Conversation.ConversationType.PRIVATE,
//                    TextMessage.obtain("我是一条来自${AppPreUtils.getString(Constant.KEY_USER_NAME)}的消息！"))
//
//                IMCenter.getInstance().sendMessage(message,null,null,
//                    object : IRongCallback.ISendMessageCallback {
//                        override fun onAttached(message: Message?) {
//
//                        }
//
//                        override fun onSuccess(message: Message?) {
//                            Toaster.showLong("发送成功！")
//                        }
//
//                        override fun onError(
//                            message: Message?,
//                            errorCode: RongIMClient.ErrorCode?
//                        ) {
//                            Toaster.showLong("发送失败  ${errorCode.toString()}！")
//                        }
//
//                    })
            }
        })

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ContactListViewModel::class.java]

        lifecycleScope.launch {
            viewModel.contactSharedFlow.collect {
                mAdapter?.setData(it.sortedWith(mComparator).toMutableList())
            }
        }
        viewModel.getContactList()

    }

    override fun onDestroy() {
        super.onDestroy()
        mRvContact = null
        mHitLetter = null
        mSideBar = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}