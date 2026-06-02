package cn.lancet.navigation.ui.me

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import cn.lancet.navigation.databinding.FragmentMeBinding
import cn.lancet.navigation.widget.LogoutDialog
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class FragmentMe : Fragment() {

    private var _binding: FragmentMeBinding? = null

    private var mUserAvatar: ShapeableImageView? = null
    private var mUserName: AppCompatTextView? = null

    private val binding get() = _binding!!

    private val launcherActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = it.data?.data
            if (uri != null) {
                binding.avatar.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        mUserAvatar = binding.avatar
        mUserName = binding.tvNickname

        setupMeInsets()
        setupDailyWallpaper()
        initEvent()
        bindGuestUserInfo()
    }

    private fun setupDailyWallpaper() {
        val appContext = requireContext().applicationContext

        MeDailyWallpaperWorkScheduler.enqueueDailyWork(appContext)
        MeDailyWallpaperWorkScheduler.enqueueOneTimeWarmUp(appContext)

        val repository = MeDailyWallpaperRepository(appContext)

        MeDailyWallpaperLoader(repository)
            .loadInto(
                imageView = binding.ivAvatarBg,
                lifecycleOwner = viewLifecycleOwner,
                alpha = 1.0f
            )
    }

    private fun setupMeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.clMeRoot) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.svMeContent.updatePadding(
                left = binding.svMeContent.paddingLeft,
                top = systemBars.top + 20.dp(),
                right = binding.svMeContent.paddingRight,
                bottom = systemBars.bottom + 150.dp()
            )

            binding.actionLogout.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin = systemBars.top + 12.dp()
                marginEnd = 16.dp()
            }

            insets
        }

        ViewCompat.requestApplyInsets(binding.clMeRoot)
    }

    private fun bindGuestUserInfo() {
        binding.avatar.load("https://cn.bing.com/th?id=ONUT.RZzpZxfJ3d0M_VAtoK_L3w&pid=News&w=120&h=96&c=14&rs=2&qlt=90&dpr=2")
        binding.tvNickname.text = "访客"
        binding.tvUserSex.text = "未知"
        binding.tvUserAge.text = "未知"
        binding.tvUserEmail.text = "未登录"
        binding.tvUserDesc.text = "已跳过登录注册校验"
    }

    private fun initEvent() {
        binding.actionLogout.visibility = View.GONE

        binding.avatar.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            launcherActivity.launch(intent)
        }

        binding.actionLogout.setOnClickListener {
            LogoutDialog.newInstance()
                .setOnLogoutListener(
                    object : LogoutDialog.OnLogoutClickListener {
                        override fun logout() {
                            // TODO: 接入真实退出登录逻辑
                        }
                    }
                )
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

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density + 0.5f).toInt()
    }
}