package cn.lancet.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.FragmentMeBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.AppPreUtils
import coil.load
import com.google.android.material.imageview.ShapeableImageView


class FragmentMe : Fragment() {

    private var _binding: FragmentMeBinding? = null

    private var mUserAvatar: ShapeableImageView? = null
    private var mUserName: AppCompatTextView? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mUserAvatar = binding.ivAvatar
        mUserName = binding.tvName

        getUserInfo()

        initEvent()

    }

    private fun initEvent() {
        binding.llSetting.setOnClickListener {
            AppPreUtils.clearAll()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun getUserInfo(){
        val queries = BmobQuery<User>()

        queries.getObject(AppPreUtils.getString(Constant.KEY_USER_ID), object : QueryListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                if (e == null) {
                    user?.let {
                        mUserAvatar?.load(it.avatar){
                            placeholder(R.mipmap.icon_default_avatar)
                            error(R.mipmap.icon_default_avatar)
                        }

                        mUserName?.text = it.name
                    }
                }
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        mUserAvatar = null
        mUserName = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}