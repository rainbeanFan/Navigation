package cn.lancet.navigation.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.ai.ChatMessageListener
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.lancet.navigation.NavigationApp
import cn.lancet.navigation.adapter.ChatAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityChatBinding
import cn.lancet.navigation.module.BmobMessage
import cn.lancet.navigation.module.SEND_BY_BOT
import cn.lancet.navigation.module.SEND_BY_ME
import cn.lancet.navigation.module.User
import com.hjq.toast.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChatActivity: AppCompatActivity(){

    private var mBinding: ActivityChatBinding? = null
    private var mSession = ""
    private var mUserName = ""
    private var mStartMessage = ""
    private var mPrompt = ""
    private var mLogo = ""
    private var mUserAvatar = ""

    private var mChatList = mutableListOf<BmobMessage>()

    private val mChatAdapter by lazy {
        ChatAdapter(mChatList, mLogo,mUserAvatar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        initEvent()
    }

    private fun initEvent(){
        mBinding?.ivBack?.setOnClickListener { finish() }
        mBinding?.tvTitle?.text = intent.getStringExtra(Constant.KEY_CHARACTER_TITLE)
        mSession = intent.getStringExtra(Constant.KEY_CHARACTER_NAME)?:""
        mPrompt = intent.getStringExtra(Constant.KEY_CHARACTER_PROMPT)?:""
        mLogo = intent.getStringExtra(Constant.KEY_CHARACTER_AVATAR)?:""

        BmobQuery<User>().getObject(BmobUser.getCurrentUser().objectId, object : QueryListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                if (e == null) {
                    mUserAvatar = user?.avatar?:""
                }
            }
        })

        val user = BmobUser.getCurrentUser()
        mUserName = user.username

        mStartMessage = "你好，我是${intent.getStringExtra(Constant.KEY_CHARACTER_TITLE)}，有什么可以帮助你的吗？"
        NavigationApp.mBmobAI?.setPrompt(mStartMessage)

        initHistoryChatList()

        mBinding?.sendBt?.setOnClickListener {
            val content  = mBinding?.messageEditText?.text?.trim().toString()
            if (content.isNullOrEmpty()){
                Toast.makeText(this,"请输入内容",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            NavigationApp.mBmobAI?.Connect()

            addToChat(content,SEND_BY_ME)
            mBinding?.messageEditText?.setText("")

            NavigationApp.mBmobAI?.Chat(content,mSession,
                object : ChatMessageListener {
                    override fun onMessage(message: String?) {
                        Log.d("ChatActivity", "onMessage: $message")
                        addToLastMessage(message?:"")
                    }

                    override fun onFinish(message: String?) {
                        Log.d("ChatActivity", "onFinish: $message")
                        val bmobMessage = BmobMessage(session = mSession, userName = mUserName, message = message ?: "", sendBy = SEND_BY_BOT)
                        bmobMessage.save(object : SaveListener<String>() {
                            override fun done(p0: String?, p1: BmobException?) {

                            }
                        })

                        mBinding?.sendBt?.isEnabled = true
                    }

                    override fun onError(error: String?) {
                        Log.d("ChatActivity", "onError: $error")
                        mBinding?.sendBt?.isEnabled = true
                    }

                    override fun onClose() {
                        Log.d("ChatActivity", "onClose: ")
                        mBinding?.sendBt?.isEnabled = true
                    }

                })

        }

    }

    private fun initHistoryChatList() {
        val bmobMessage = BmobMessage(session = mSession, userName = mUserName, message = mStartMessage, sendBy = SEND_BY_BOT)
        mChatList.add(0, bmobMessage)

        val query = BmobQuery<BmobMessage>()
        query.addWhereEqualTo("userName", mUserName)
        query.addWhereEqualTo("session", mSession)
        query.findObjects(object : FindListener<BmobMessage>() {
            override fun done(list: MutableList<BmobMessage>?, e: BmobException?) {
                if (e == null && list!=null) {
                    mChatList.addAll(list)

                    mBinding?.chatRecyclerView?.adapter = mChatAdapter


                } else {
                    Toaster.show("获取历史消息失败")

                }
            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addToLastMessage(content:String){
        lifecycleScope.launch(Dispatchers.Main){
            if (mChatList.isNotEmpty()){
                val message = mChatList.last()
                if (message.sendBy == SEND_BY_ME){
                    mChatList.add(BmobMessage(session = mSession, userName = mUserName, message = content, sendBy = SEND_BY_BOT))
                }else{
                    message.message += content
                }
                mBinding?.chatRecyclerView?.smoothScrollToPosition(mChatAdapter.itemCount)
                mChatAdapter.notifyDataSetChanged()
            }
        }
    }


    fun addToChat(content: String,sendBy: String){
        lifecycleScope.launch(Dispatchers.Main){
            val message = BmobMessage(session = mSession, userName = mUserName, message = content, sendBy = sendBy)
            message.save(object : SaveListener<String>() {
                override fun done(result: String?, e: BmobException?) {
                    if (e != null) {
                        Toaster.show("保存失败")
                        Log.e("ChatActivity", "done: ${e.message}")
                    }
                }
            })
            mChatList.add(message)
            mChatAdapter.notifyDataSetChanged()
            mBinding?.chatRecyclerView?.smoothScrollToPosition(mChatAdapter.itemCount)
        }
    }


    fun clearSession(){
        NavigationApp.mBmobAI?.Clear(mSession)
        mChatList.clear()
        mChatAdapter.notifyDataSetChanged()


    }


}
