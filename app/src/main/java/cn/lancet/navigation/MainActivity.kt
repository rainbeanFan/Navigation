package cn.lancet.navigation


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cn.lancet.navigation.databinding.ActivityMainBinding
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.Toaster

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null

    private var mNavController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        ImmersionBar.with(this).init()
        ImmersionBar.with(this).statusBarDarkFont(true).init()

        mBinding?.fabCreateNotice?.setOnClickListener {
            Toaster.show("创建新的角色")
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


}