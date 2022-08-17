package cn.lancet.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import cn.lancet.navigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding:ActivityMainBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        val navHostFragment = NavHostFragment.create(R.navigation.lancet_navigation)

        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()

        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragmentC) {
                mBinding!!.toolbar.visibility = View.GONE
            } else {
                mBinding!!.toolbar.visibility = View.VISIBLE
            }
        }

        val appBarConfiguration = AppBarConfiguration
            .Builder(navController.graph).build()
//            .Builder().setFallbackOnNavigateUpListener {
//            if (navController.navigateUp()) {
//                true
//            } else {
//                finish()
//                false
//            }
//        }.build()



        setSupportActionBar(mBinding!!.toolbar)
        NavigationUI.setupWithNavController(mBinding!!.toolbar,navController,appBarConfiguration)

        NavigationUI.setupWithNavController(mBinding!!.bottomNavigationView,navController)

    }

}