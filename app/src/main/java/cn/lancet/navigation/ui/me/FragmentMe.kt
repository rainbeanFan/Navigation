package cn.lancet.navigation.ui.me

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.R
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.databinding.FragmentMeBinding
import cn.lancet.navigation.widget.CommentBottomDialog
import cn.lancet.navigation.widget.LogoutDialog
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.launch


class FragmentMe : Fragment() {

    private var _binding: FragmentMeBinding? = null

    private var mUserAvatar: ShapeableImageView? = null
    private var mUserName: AppCompatTextView? = null

    private lateinit var viewModel: MeViewModel

    private val launcherActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val mUri = it.data?.data
            binding.avatar.setImageURI(mUri)
        }
    }

    private val binding get() = _binding!!

    private var mDialog: CommentBottomDialog? = null

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

        initEvent()

        if (BmobUser.isLogin()) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED){
                    try {
                        val userInfo = viewModel.getUserInfo()
                        binding.avatar.load(userInfo.avatar) {
                            crossfade(true)
                            placeholder(R.mipmap.icon_default_avatar)
                            error(R.mipmap.icon_default_avatar)
                        }
                        binding.tvNickname.text = userInfo.name
                        binding.tvUserSex.text = userInfo.RCToken?:"未知"
                        binding.tvUserEmail.text = userInfo.name
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

            }
        }

    }

    private fun initEvent() {

        binding.actionLogout.visibility = if (BmobUser.isLogin()) View.VISIBLE else View.GONE

        binding.avatar.setOnClickListener {
//            XXPermissions.with(this)
////                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
//                .permission(
//                    Permission.READ_MEDIA_IMAGES,
//                    Permission.READ_MEDIA_VIDEO,
//                    Permission.READ_MEDIA_AUDIO
//                )
//                .request { permissions, allGranted ->
//                    if (allGranted) {
//                        val intent = Intent(
//                            Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                        )
//                        launcherActivity.launch(intent)
//                    }
//
//                }

            if (mDialog == null){
                mDialog = CommentBottomDialog(noticeId = "123")
            }
            mDialog?.show(requireActivity().supportFragmentManager, "COMMENT")

            Handler(Looper.getMainLooper()).postDelayed({
                LogoutDialog.newInstance()
                    .setOnLogoutListener(object : LogoutDialog.OnLogoutClickListener {
                        override fun logout() {
                            if (mDialog!=null && mDialog!!.showsDialog && !mDialog!!.isRemoving){
                                mDialog?.dismissAllowingStateLoss()
                            }
                            if (mDialog != null){
                                mDialog = null
                            }
                            if (mDialog == null){
                                mDialog = CommentBottomDialog(noticeId = "123")
                            }
                            mDialog?.show(requireActivity().supportFragmentManager, "COMMENT")
//                        BmobUser.logOut()
//                        startActivity(Intent(requireContext(), LoginActivity::class.java))
//                        activity?.finish()
                        }
                    })
                    .show(requireActivity().supportFragmentManager, "LOGOUT")
            }, 3000)

        }


        binding.actionLogout.setOnClickListener {
            LogoutDialog.newInstance()
                .setOnLogoutListener(object : LogoutDialog.OnLogoutClickListener {
                    override fun logout() {
                        if (mDialog!=null && mDialog!!.showsDialog && !mDialog!!.isRemoving){
                            mDialog?.dismissAllowingStateLoss()
                        }
                        if (mDialog != null){
                            mDialog = null
                        }
                        if (mDialog == null){
                            mDialog = CommentBottomDialog(noticeId = "123")
                        }
                        mDialog?.show(requireActivity().supportFragmentManager, "COMMENT")
//                        BmobUser.logOut()
//                        startActivity(Intent(requireContext(), LoginActivity::class.java))
//                        activity?.finish()
                    }
                })
                .show(requireActivity().supportFragmentManager, "LOGOUT")
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.stateFlow.collect {
                    binding.tvUserSex.text = it.toString()
                }
            }
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