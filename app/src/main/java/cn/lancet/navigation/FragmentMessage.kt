package cn.lancet.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobQueryResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SQLQueryListener
import cn.bmob.v3.listener.SaveListener
import cn.lancet.navigation.adapter.NoticeAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.FragmentABinding
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.module.User
import cn.lancet.navigation.notice.NoticeDetailActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FragmentMessage : Fragment() {

    private var _binding: FragmentABinding? = null

    private var mRvMessage: RecyclerView? = null

    private var mAdapter: NoticeAdapter? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        Log.d("onCreate  ", "Fragment A")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("onCreateView  ", "Fragment A")
        _binding = FragmentABinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume  ", "Fragment A")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("onViewCreated  ", "Fragment A")


        mAdapter = NoticeAdapter(requireContext())
        mRvMessage = binding.rvMessage
        mRvMessage?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
        mAdapter?.setOnItemClickListener(object : NoticeAdapter.OnItemClickListener {
            override fun onItemClick(notice: Notice) {
                val intent = Intent(requireContext(),NoticeDetailActivity::class.java)
                intent.putExtra(Constant.KEY_NOTICE_ID,notice.objectId)
                startActivity(intent)
            }
        })
        getNotices()
    }

    private fun getNotices() {

        val queries = BmobQuery<Notice>()

        val bqlString = "select * from Notice order by -createdTime"

        queries.doSQLQuery(bqlString, object : SQLQueryListener<Notice>() {
            override fun done(notices: BmobQueryResult<Notice>?, e: BmobException?) {
                notices?.let {
                    mAdapter?.setData(it.results)
                }
            }

        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String){
        when(event) {
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