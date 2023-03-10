package cn.lancet.navigation.rest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.lancet.navigation.R
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityNoticeDetailBinding
import cn.lancet.navigation.util.TimeUtil
import coil.load
import com.hjq.toast.Toaster
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus


class NoticeDetailActivity:AppCompatActivity() {

    private lateinit var binding: ActivityNoticeDetailBinding

    private var mRestId: String? = null

    private lateinit var viewModel: NoticeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[NoticeViewModel::class.java]
        mRestId = intent?.getStringExtra(Constant.KEY_REST_ID)
        initEvent()
    }

    private fun initEvent() {
        if (mRestId.isNullOrBlank()){
            return
        }
        viewModel.getNoticeDetail(mRestId!!)
        lifecycleScope.launch {
            viewModel.noticeFlow.collect{
                binding.tvNotificationTitle.text = it.noticeTitle
                binding.ivNotificationCover.load(it.coverImageUrl){
                    placeholder(R.mipmap.ic_notice_place_holder)
                    error(R.mipmap.ic_notice_place_holder)
                }
                binding.tvNoticeContent.text = it.noticeContent
                binding.tvNoticePublisher.text = it.publisher
                binding.tvNoticePublisherTime.text = it.createdTime?.let { time -> TimeUtil.ctsToTimeStr(time) }
            }
        }

        binding.ivDelete.setOnClickListener {
            mRestId?.let {
                viewModel.deleteNotice(it)
            }
        }

        lifecycleScope.launch {
            viewModel.deleteFlow.collect{
                if (it){
                    EventBus.getDefault().post("deleteNotice")
                    finish()
                }else{
                    Toaster.showLong("Delete notice failed")
                }
            }
        }
        binding.ivBack.setOnClickListener { finish() }
    }


}