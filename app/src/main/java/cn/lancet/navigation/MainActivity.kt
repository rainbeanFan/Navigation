package cn.lancet.navigation


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FetchUserInfoListener
import cn.lancet.discovery.DiscoveryActivity
import cn.lancet.discovery.FindActivity
import cn.lancet.navigation.databinding.ActivityMainBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.rest.RestHomeActivity
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.Toaster
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

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
            startActivity(Intent(this, FindActivity::class.java))
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

    private val mNums = mutableListOf(1,3,5,7,9)
    private fun getUserInfo() {

        lifecycleScope.launch {
            flow {
                (1..1000).forEach {
                    delay(100)
                    emit(it)
                }
            }.conflate().collect {
                delay(250)
                Log.d("Filter Result==","${it}")
            }
        }


        val isEmptyList = mNums.any {
            it > 9
        }

        Log.d("Filter Result==","${isEmptyList}")

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