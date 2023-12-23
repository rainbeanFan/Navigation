package cn.lancet.navigation.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.bmob.v3.ai.BmobAI
import cn.lancet.navigation.NavigationApp
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityChatBinding
import cn.lancet.navigation.module.BmobMessage
import cn.lancet.navigation.module.SEND_BY_BOT

class ChatActivity: AppCompatActivity(){

    private var mBinding: ActivityChatBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        initEvent()
    }

    private fun initEvent(){
        mBinding?.ivBack?.setOnClickListener { finish() }
        mBinding?.tvTitle?.text = intent.getStringExtra(Constant.KEY_CHARACTER_TITLE)

        NavigationApp().mBmobAI?.setPrompt("你好，我是${intent.getStringExtra(Constant.KEY_CHARACTER_TITLE)}，有什么可以帮助你的吗？")

        initHistoryChatList()

    }

    private fun initHistoryChatList() {
        val bmobMessage = BmobMessage("", SEND_BY_BOT, "", "")




    }

}
