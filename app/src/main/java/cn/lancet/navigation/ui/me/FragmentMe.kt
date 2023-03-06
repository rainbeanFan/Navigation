package cn.lancet.navigation.ui.me

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
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.R
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.databinding.FragmentMeBinding
import cn.lancet.navigation.util.AppPreUtils
import cn.lancet.navigation.widget.LogoutDialog
import coil.load
import coil.transform.BlurTransformation
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FragmentMe : Fragment() {

    private var _binding: FragmentMeBinding? = null

    private var mUserAvatar: ShapeableImageView? = null
    private var mUserName: AppCompatTextView? = null

    private lateinit var viewModel: MeViewModel

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }

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

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MeViewModel::class.java]

        mUserAvatar = binding.avatar
        mUserName = binding.name

        initEvent()

        viewModel.getUserInfo()

        lifecycleScope.launch {
            viewModel.sharedFlow.collect {
                binding.name.text = it.name
                binding.avatar.load(it.avatar){
                    placeholder(R.mipmap.icon_default_avatar)
                    error(R.mipmap.icon_default_avatar)
                }
                binding.ivAvatarBg.load(it.avatar){
                    placeholder(R.mipmap.splash)
                    error(R.mipmap.splash)
                    .transformations(BlurTransformation(requireContext(),5F,10F))
                }
                binding.description.text = it.email
            }
        }

    }

    private fun initEvent() {

        binding.goDetail.setOnClickListener {
            startActivity(Intent(requireContext(),UserInfoActivity::class.java))
        }

        binding.actionLogout.setOnClickListener {
            LogoutDialog.newInstance()
                .setOnLogoutListener(object : LogoutDialog.OnLogoutClickListener {
                    override fun logout() {
                        BmobUser.logOut()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        activity?.finish()
                    }
                })
                .show(requireActivity().supportFragmentManager,"LOGOUT")
        }
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