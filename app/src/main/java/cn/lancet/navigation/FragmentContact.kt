package cn.lancet.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.lancet.navigation.adapter.ContactAdapter
import cn.lancet.navigation.databinding.FragmentBBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.FirstLetterComparator
import cn.lancet.navigation.widget.SideBar
import java.util.Locale


class FragmentContact : Fragment() {

    private var _binding: FragmentBBinding? = null

    private var mRvContact: RecyclerView? = null

    private var mHitLetter:AppCompatTextView?=null

    private var mSideBar:SideBar?=null

    private var mAdapter: ContactAdapter? = null

    private var mComparator = FirstLetterComparator()

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBBinding.inflate(inflater, container, false)
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
        mSideBar?.setOnTouchingLetterChangedListener(object : SideBar.OnTouchingLetterChangedListener {
            override fun onTouchingLetterChanged(letter: String?) {
                val position = letter?.let { mAdapter?.getPositionForSection(it.codePointAt(0)) }
                if (position!=null && position!=-1){
                    (mRvContact?.layoutManager as LinearLayoutManager).scrollToPosition(position)
                }
            }

        })

        val queries = BmobQuery<User>()

        queries.findObjects(object : FindListener<User>() {
            override fun done(users: MutableList<User>?, e: BmobException?) {
                if (e == null && users != null) {

                    users.forEach {
                        it.sort_letter =
                            if (it.name!!.substring(0,1).uppercase(Locale.getDefault()).matches("[A-Z]".toRegex())){
                                it.name.substring(0,1).uppercase(Locale.getDefault())
                            }else{
                                "#"
                            }
                    }

                    mComparator.let {
                        mAdapter?.setData(users.sortedWith(it).toMutableList())
                    }

                }
            }

        })

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