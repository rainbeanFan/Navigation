package cn.lancet.navigation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
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

        supportFragmentManager.commitNow {
            this.replace(R.id.nav_host_fragment, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
        }


        mNavController = navHostFragment.navController

        mNavController?.navigatorProvider?.addNavigator(
            FixNavigator(this,supportFragmentManager,R.id.nav_host_fragment)
        )

        mNavController?.addOnDestinationChangedListener { _, destination, _ ->
            mBinding!!.toolbar.isTitleCentered = destination.id == R.id.fragmentC
        }


        val appBarConfiguration = AppBarConfiguration
            .Builder(mNavController!!.graph).build()

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