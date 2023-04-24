package cn.lancet.navigation


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FetchUserInfoListener
import cn.lancet.navigation.databinding.ActivityMainBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.rest.RestHomeActivity
import cn.lancet.navigation.widget.HotDialog
import com.gyf.immersionbar.ImmersionBar

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null

    private var mNavController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        ImmersionBar.with(this).init()

        getUserInfo()

        mBinding?.fabCreateNotice?.setOnClickListener {
//            startActivity(Intent(this, MotionLayoutActivity::class.java))
//            startActivity(Intent(this, RestHomeActivity::class.java))

            HotDialog().show(supportFragmentManager,"HOT")

        }

        val navHostFragment = NavHostFragment.create(R.navigation.lancet_navigation)

        supportFragmentManager.commitNow {
            this.replace(R.id.nav_host_fragment, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
        }


        mNavController = navHostFragment.navController

        mNavController?.navigatorProvider?.addNavigator(
            FixNavigator(this, supportFragmentManager, R.id.nav_host_fragment)
        )

        NavigationUI.setupWithNavController(mBinding!!.bottomNavigationView, mNavController!!)
    }


    private fun getUserInfo() {
        if (BmobUser.isLogin()) {
            BmobUser.fetchUserInfo(object : FetchUserInfoListener<User>() {
                override fun done(user: User?, e: BmobException?) {
                    if (e == null) {
                        BmobUser.getCurrentUser(User::class.java)
                    }
                }
            })
        }
    }

}