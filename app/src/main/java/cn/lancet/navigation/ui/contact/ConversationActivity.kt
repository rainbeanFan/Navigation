package cn.lancet.navigation.ui.contact

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.lancet.navigation.R
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityConversationBinding
import io.rong.imkit.conversation.ConversationFragment

class ConversationActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityConversationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val name = intent?.getStringExtra(Constant.KEY_USER_NAME)

        mBinding.ivBack.setOnClickListener { finish() }
        mBinding.tvTitle.text = name

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ConversationFragment())
            .commit()

    }



}