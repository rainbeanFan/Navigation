package cn.lancet.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.lancet.navigation.adapter.ContactAdapter
import cn.lancet.navigation.databinding.FragmentBBinding
import cn.lancet.navigation.module.User


class FragmentB : Fragment() {

    private var _binding: FragmentBBinding? = null

    private var mRvContact: RecyclerView? = null

    private var mAdapter: ContactAdapter? = null

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
        mAdapter = ContactAdapter(requireContext())

        initData()

    }

    private fun initData() {
        mRvContact?.layoutManager = LinearLayoutManager(requireContext())

        mRvContact?.adapter = mAdapter

        val queries = BmobQuery<User>()

        queries.findObjects(object : FindListener<User>() {
            override fun done(users: MutableList<User>?, e: BmobException?) {
                if (e == null && users != null) {
                    mAdapter?.setData(users)
                }
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        mRvContact = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}