package cn.lancet.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.lancet.navigation.databinding.ActivityMotionBinding
import cn.lancet.navigation.databinding.ActivityMultistateBinding

class MotionLayoutActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityMultistateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMultistateBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

}