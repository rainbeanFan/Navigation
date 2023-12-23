package cn.lancet.navigation.ui.me

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.lancet.navigation.R
import cn.lancet.navigation.databinding.ActivityUserInfoBinding
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import kotlin.math.abs

class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding

    private lateinit var viewModel: UserInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserInfoViewModel::class.java]

        viewModel.getUserInfo()

        initEvent()

    }

    private fun initEvent() {

        binding.actionBack.setOnClickListener { finish() }

        val tabs = resources.getStringArray(R.array.profile_tabs)

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = tabs.size

            override fun createFragment(position: Int): Fragment {
                return ProfileListFragment.newInstance("TAB_FEED")
            }
        }

        TabLayoutMediator(
            binding.tabLayout, binding.viewPager, false
        ) { tab, position -> tab.text = tabs[position] }.attach()

        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val expand = abs(verticalOffset) < (appBarLayout?.totalScrollRange ?: 0)
            if (expand) {
                binding.topAuthorAvatar.visibility = View.GONE
                binding.topAuthorName.visibility = View.GONE
                binding.topAuthorNameLarge.visibility = View.VISIBLE
            } else {
                binding.topAuthorAvatar.visibility = View.VISIBLE
                binding.topAuthorName.visibility = View.VISIBLE
                binding.topAuthorNameLarge.visibility = View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.sharedFlow.collect {
                binding.topAuthorAvatar.load(it.avatar) {
                    placeholder(R.mipmap.icon_default_avatar)
                    error(R.mipmap.icon_default_avatar)
                }
                binding.authorAvatarLarge.load(it.avatar) {
                    placeholder(R.mipmap.icon_default_avatar)
                    error(R.mipmap.icon_default_avatar)
                }
                binding.topAuthorNameLarge.text = it.name
                binding.topAuthorName.text = it.name
                binding.tvEmail.text = it.name
            }
        }

    }


}