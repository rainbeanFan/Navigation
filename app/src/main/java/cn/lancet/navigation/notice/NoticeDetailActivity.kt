package cn.lancet.navigation.notice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener
import cn.lancet.navigation.R
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityNoticeDetailBinding
import cn.lancet.navigation.module.Notice
import cn.lancet.navigation.util.TimeUtil
import coil.load
import org.greenrobot.eventbus.EventBus


class NoticeDetailActivity:AppCompatActivity() {

    private lateinit var binding: ActivityNoticeDetailBinding

    private var mNoticeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mNoticeId = intent?.getStringExtra(Constant.KEY_NOTICE_ID)

        initEvent()

    }

    private fun initEvent() {

        if (mNoticeId.isNullOrBlank()){
            return
        }
        getNoticeDetail()

        binding.ivDelete.setOnClickListener {
            deleteNotice()
        }

        binding.ivBack.setOnClickListener { finish() }

    }

    private fun getNoticeDetail(){
        val query = BmobQuery<Notice>()
        query.getObject(mNoticeId, object : QueryListener<Notice>() {
            override fun done(notice: Notice?, e: BmobException?) {
                if (e == null) {
                    notice?.let {

                        binding.tvNotificationTitle.text = it.noticeTitle
                        binding.ivNotificationCover.load(it.coverImageUrl){
                            placeholder(R.mipmap.ic_notice_place_holder)
                            error(R.mipmap.ic_notice_place_holder)
                        }

                        binding.tvNoticeContent.text = it.noticeContent
                        binding.tvNoticePublisher.text = it.publisher
                        binding.tvNoticePublisherTime.text = it.createdTime?.let { it1 ->
                            TimeUtil.ctsToTimeStr(
                                it1
                            )
                        }



                    }
                }
            }

        })
    }

    private fun deleteNotice(){
        val notice = Notice()
        notice.delete(mNoticeId, object : UpdateListener() {
            override fun done(e: BmobException?) {
                if (e == null){
                    EventBus.getDefault().post("deleteNotice")
                    finish()
                }
            }

        })
    }


}