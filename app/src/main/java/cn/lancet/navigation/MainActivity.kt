package cn.lancet.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import cn.lancet.navigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null

    private var mNavController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        val navHostFragment = NavHostFragment.create(R.navigation.lancet_navigation)

        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()

        mNavController = navHostFragment.navController

        mNavController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragmentC) {
                mBinding!!.toolbar.visibility = View.GONE
            } else {
                mBinding!!.toolbar.visibility = View.VISIBLE
            }
        }

        val appBarConfiguration = AppBarConfiguration
            .Builder(mNavController!!.graph).build()
//            .Builder().setFallbackOnNavigateUpListener {
//            if (navController.navigateUp()) {
//                true
//            } else {
//                finish()
//                false
//            }
//        }.build()

        setSupportActionBar(mBinding!!.toolbar)
        NavigationUI.setupWithNavController(
            mBinding!!.toolbar,
            mNavController!!,
            appBarConfiguration
        )

        NavigationUI.setupWithNavController(mBinding!!.bottomNavigationView, mNavController!!)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item) || NavigationUI.onNavDestinationSelected(
            item,
            mNavController!!
        )
    }


}