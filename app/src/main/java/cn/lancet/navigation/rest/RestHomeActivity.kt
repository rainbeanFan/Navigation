package cn.lancet.navigation.rest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import cn.lancet.navigation.PlantInfoAdapter
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityRestHomeBinding
import cn.lancet.navigation.module.RestType


class RestHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestHomeBinding

    private lateinit var viewModel: NoticeViewModel

    private lateinit var mAdapter: RestHomeAdapter

    private var mData = mutableListOf<RestType>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[NoticeViewModel::class.java]

        binding.ivBack.setOnClickListener { finish() }

        initEvent()
    }

    private fun initEvent() {
        mData.clear()
        mData.add(RestType().apply {
            name = "植物识别"
            url = ""
            type = 0
        })
        mData.add(RestType().apply {
            name = "果蔬识别"
            url = ""
            type = 1
        })
        mData.add(RestType().apply {
            name = "动物识别"
            url = ""
            type = 2
        })
        mData.add(RestType().apply {
            name = "菜品识别"
            url = ""
            type = 3
        })
        mData.add(RestType().apply {
            name = "车型识别"
            url = ""
            type = 4
        })

        mAdapter = RestHomeAdapter(this).apply {
            setListener(object : RestHomeAdapter.OnRestTypeClickListener {
                override fun onItemClick(restType: RestType) {
                    val intent = Intent(this@RestHomeActivity,RestActivity::class.java)
                    intent.putExtra(Constant.KEY_REST_TYPE,restType.type)
                    startActivity(intent)
                    finish()
                }
            })
        }
        binding.rvRest.layoutManager = GridLayoutManager(this, 2)
        binding.rvRest.adapter = mAdapter
        mAdapter.setData(mData)
    }


}