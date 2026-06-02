package cn.lancet.navigation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.adapter.CharacterAdapter
import cn.lancet.navigation.bing.BingWallpaperRepository
import cn.lancet.navigation.bing.HomeWallpaperLoader
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.FragmentHomeBinding
import cn.lancet.navigation.flutter.FlutterTreeHole
import cn.lancet.navigation.module.Character
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FragmentHome : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private var mRvPrompt: RecyclerView? = null

    private val mAdapter: CharacterAdapter by lazy {
        CharacterAdapter(requireContext())
    }

    private lateinit var viewModel: PlantListViewModel

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[PlantListViewModel::class.java]

        setupHomeInsets()
        setupHomeWallpaper()

        mRvPrompt = binding.rvMessage
        mRvPrompt?.apply { adapter = mAdapter }
        mAdapter.setOnItemClickListener(object : CharacterAdapter.OnItemClickListener {
            override fun onItemClick(character: Character) {
                val nativeIntent = Intent(requireContext(), ChatActivity::class.java)
                    .apply {
                        putExtra(Constant.KEY_CHARACTER_TITLE, character.cnName)
                        putExtra(Constant.KEY_CHARACTER_PROMPT, character.prompt)
                        putExtra(Constant.KEY_CHARACTER_NAME, character.enName)
                        putExtra(Constant.KEY_CHARACTER_AVATAR, character.avatar)
                        putExtra(Constant.KEY_CHARACTER_WELCOME, character.welTips)
                    }

                val intent = if (FlutterTreeHole.isTreeHole(character) && FlutterTreeHole.isAvailable()) {
                    FlutterTreeHole.createIntent(requireContext(), character)
                } else {
                    nativeIntent
                }
                startActivity(intent)
            }
        })


        mAdapter.setData(
            mutableListOf(
                Character(
                    userNum = "访客模式",
                    cnName = "树洞",
                    enName = "guest",
                    avatar = "https://cn.bing.com/th?id=ONUT.RZzpZxfJ3d0M_VAtoK_L3w&pid=News&w=120&h=96&c=14&rs=2&qlt=90&dpr=2",
                    prompt = "随便聊聊",
                    description = "登录注册校验已跳过，可以直接进入主界面。",
                    welTips = "你好，有什么想聊的吗？",
                    rank = 0
                )
            )
        )
        lifecycleScope.launch {
            viewModel.mCharacterInfoFlow.collect {
                if (it.isNotEmpty()) {
                    mAdapter.setData(it)
                }
            }
        }

    }

    private fun setupHomeWallpaper() {
        val repository = BingWallpaperRepository(
            context = requireContext().applicationContext
        )

        HomeWallpaperLoader(repository)
            .loadInto(
                imageView = binding.ivHomeBingBg,
                lifecycleOwner = viewLifecycleOwner,
                alpha = 1.0f
            )
    }

    private fun setupHomeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.clHomeRoot) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.rvMessage.updatePadding(
                left = binding.rvMessage.paddingLeft,
                top = systemBars.top + 20f.dp().toInt(),
                right = binding.rvMessage.paddingRight,
                bottom = systemBars.bottom + 150f.dp().toInt()
            )

            insets
        }
    }

    private fun Float.dp(): Float {
        return this * resources.displayMetrics.density
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: String) {
        when (event) {
            "add character" -> {

            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mRvPrompt = null
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
