package cn.lancet.navigation.notice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.lancet.navigation.R
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityNoticeDetailBinding
import cn.lancet.navigation.util.TimeUtil
import coil.load
import com.hjq.toast.Toaster
import org.greenrobot.eventbus.EventBus


class NoticeDetailActivity:AppCompatActivity() {

    private lateinit var binding: ActivityNoticeDetailBinding

    private var mNoticeId: String? = null

    private lateinit var viewModel: NoticeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[NoticeViewModel::class.java]

        mNoticeId = intent?.getStringExtra(Constant.KEY_NOTICE_ID)

        initEvent()

    }

    private fun initEvent() {

        if (mNoticeId.isNullOrBlank()){
            return
        }

        viewModel.getNoticeDetail(mNoticeId!!)

        viewModel.noticeLiveData.observe(this){
            binding.tvNotificationTitle.text = it.noticeTitle
            binding.ivNotificationCover.load(it.coverImageUrl){
                placeholder(R.mipmap.ic_notice_place_holder)
                error(R.mipmap.ic_notice_place_holder)
            }

            binding.tvNoticeContent.text = it.noticeContent
            binding.tvNoticePublisher.text = it.publisher
            binding.tvNoticePublisherTime.text = it.createdTime?.let { time -> TimeUtil.ctsToTimeStr(time) }
        }

        binding.ivDelete.setOnClickListener {
            mNoticeId?.let {
                viewModel.deleteNotice(it)
            }
        }

        viewModel.deleteNotice.observe(this){
            if (it){
                EventBus.getDefault().post("deleteNotice")
                finish()
            }else{
                Toaster.showLong("Delete notice failed")
            }
        }

        binding.ivBack.setOnClickListener { finish() }
    }


}