package cn.lancet.navigation.ui.me

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import cn.lancet.navigation.R
import cn.lancet.navigation.account.LoginActivity
import cn.lancet.navigation.databinding.FragmentMeBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.FileUtils
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
            val filePath = FileUtils.getFilePath(requireContext(), mUri)
            viewModel.modifyAvatar(filePath)
        }
    }

    private val binding get() = _binding!!

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
            viewModel.getUserInfo()
        }

//        lifecycleScope.launch {
//            viewModel.sharedFlow.collect {
//                binding.avatar.load(it.avatar) {
//                    placeholder(R.mipmap.icon_default_avatar)
//                    error(R.mipmap.icon_default_avatar)
//                }
//            }
//        }

        lifecycleScope.launch {
            viewModel.userAvatarFlow.collect {
                val currentUser = BmobUser.getCurrentUser(User::class.java)
                currentUser.avatar = it.image
                currentUser.update(object : UpdateListener() {
                    override fun done(e: BmobException?) {
                        if (e == null){

                        }else{
                            Log.d("Lancet  ", e.toString())
                        }
                    }
                })

                val decodedString = Base64.decode(it.image,Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.size)
                binding.avatar.setImageBitmap(bitmap)
            }
        }
    }

    private fun initEvent() {

//        binding.avatar.setOnClickListener {
//            XXPermissions.with(this)
//                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
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
//        }


        binding.actionLogout.setOnClickListener {
            LogoutDialog.newInstance()
                .setOnLogoutListener(object : LogoutDialog.OnLogoutClickListener {
                    override fun logout() {
                        BmobUser.logOut()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        activity?.finish()
                    }
                })
                .show(requireActivity().supportFragmentManager, "LOGOUT")
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