package cn.lancet.navigation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.lancet.navigation.R
import cn.lancet.navigation.databinding.LayoutChatItemBinding
import cn.lancet.navigation.module.BmobMessage
import cn.lancet.navigation.module.SEND_BY_ME
import coil.load

class ChatAdapter(val messageList:MutableList<BmobMessage>, val logo:String, val userAvatar:String):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_chat_item, null)
        return ChatViewHolder(LayoutChatItemBinding.bind(view))
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMsg = messageList[position]
        if (chatMsg.sendBy == SEND_BY_ME){
            holder.binding.tvEndChatItemName.text = chatMsg.message
            holder.binding.llEndChatItemRoot.visibility = View.VISIBLE
            holder.binding.llStartChatItemRoot.visibility = View.GONE
            holder.binding.ivEndChatItemAvatar.load(userAvatar){
                placeholder(R.mipmap.icon_default_avatar)
                error(R.mipmap.icon_default_avatar)
            }
        }else{
            holder.binding.tvStartChatItemName.text = chatMsg.message
            holder.binding.llEndChatItemRoot.visibility = View.GONE
            holder.binding.llStartChatItemRoot.visibility = View.VISIBLE
            holder.binding.ivStartChatItemAvatar.load(logo){
                placeholder(R.mipmap.icon_default_avatar)
                error(R.mipmap.icon_default_avatar)
            }
        }

    }


    class ChatViewHolder(val binding: LayoutChatItemBinding): RecyclerView.ViewHolder(binding.root)


}