package cn.lancet.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.lancet.navigation.databinding.ActivityMotionBinding

class MotionLayoutActivity: AppCompatActivity() {

    private lateinit var mBinding: ActivityMotionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMotionBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

}