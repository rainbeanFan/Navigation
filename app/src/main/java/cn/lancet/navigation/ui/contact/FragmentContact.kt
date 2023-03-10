package cn.lancet.navigation.ui.contact


import android.content.Intent
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
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.adapter.ContactAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.FragmentContactBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.FirstLetterComparator
import cn.lancet.navigation.widget.SideBar
import com.google.android.material.button.MaterialButton
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

    private var mBtnLogin:MaterialButton? = null

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
        mBtnLogin = binding.btnLogin

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

//                RouteUtils.registerActivity(RouteUtils.RongActivityType.ConversationActivity,
//                    ConversationActivity::class.java)

//                val intent = Intent(requireContext(),ConversationActivity::class.java).apply {
//                    putExtra(RouteUtils.CONVERSATION_TYPE,Conversation.ConversationType.PRIVATE)
//                    putExtra(RouteUtils.TARGET_ID,user.objectId)
//                }

                val bundle = Bundle().apply {
                    putString(Constant.KEY_USER_NAME,user.name)
                }

//               requireContext().startActivity(intent)

//                RouteUtils.routeToConversationActivity(
//                    requireContext(),
//                    Conversation.ConversationType.PRIVATE,
//                    user.objectId,bundle
//                )
            }
        })

        mBtnLogin?.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ContactListViewModel::class.java]

        lifecycleScope.launch {
            viewModel.contactSharedFlow.collect {
                mAdapter?.setData(it.sortedWith(mComparator).toMutableList())
            }
        }
        if (BmobUser.isLogin()){
            viewModel.getContactList()
            mRvContact?.visibility = View.VISIBLE
            mSideBar?.visibility = View.VISIBLE
            mBtnLogin?.visibility = View.GONE
        }else{
            mRvContact?.visibility = View.GONE
            mSideBar?.visibility = View.GONE
            mBtnLogin?.visibility = View.VISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mRvContact = null
        mHitLetter = null
        mSideBar = null
        mBtnLogin = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}