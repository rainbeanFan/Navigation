package cn.lancet.navigation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobUser
import cn.lancet.navigation.adapter.CharacterAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.FragmentHomeBinding
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



        mRvPrompt = binding.rvMessage
        mRvPrompt?.apply { adapter = mAdapter }
        mAdapter.setOnItemClickListener(object : CharacterAdapter.OnItemClickListener {
            override fun onItemClick(character: Character) {
                val intent = Intent(requireContext(), ChatActivity::class.java)
                    .apply {
                        putExtra(Constant.KEY_CHARACTER_TITLE, character.cnName)
                        putExtra(Constant.KEY_CHARACTER_PROMPT, character.prompt)
                        putExtra(Constant.KEY_CHARACTER_NAME, character.enName)
                        putExtra(Constant.KEY_CHARACTER_AVATAR, character.avatar)
                        putExtra(Constant.KEY_CHARACTER_WELCOME, character.welTips)
                    }
                startActivity(intent)
            }
        })


        if (BmobUser.isLogin()){
            viewModel.getCharacterInfo()
            lifecycleScope.launch {
                viewModel.mCharacterInfoFlow.collect {
                    mAdapter.setData(it)
                }
            }
        }else{

        }

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