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
import cn.lancet.navigation.R
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.databinding.FragmentMeBinding
import cn.lancet.navigation.util.AppPreUtils
import coil.load
import com.google.android.material.imageview.ShapeableImageView


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

        mUserAvatar = binding.ivAvatar
        mUserName = binding.tvName

        viewModel.getUserInfo()

        viewModel.userLiveData.observe(viewLifecycleOwner){
            binding.tvName.text = it.name
            binding.ivAvatar.load(it.avatar){
                placeholder(R.mipmap.icon_default_avatar)
                error(R.mipmap.icon_default_avatar)
            }
        }

        initEvent()
    }

    private fun initEvent() {
        binding.llSetting.setOnClickListener {
            AppPreUtils.clearAll()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
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