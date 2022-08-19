package cn.lancet.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

/**
 * Created by Lancet at 2022/8/19 11:06
 */

@Navigator.Name("fixNavigator")
class FixNavigator(private val context: Context,
private val fragmentManager: FragmentManager,
private val containerId:Int):FragmentNavigator(
    context, fragmentManager, containerId
) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination {

        val fragmentTransaction = fragmentManager.beginTransaction()
        var fragment = fragmentManager.primaryNavigationFragment
        if (fragment!=null){
            fragmentTransaction.hide(fragment)
        }
        val tag = destination.id.toString()
        fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment!=null){
            fragmentTransaction.show(fragment)
        }else{
            fragment = fragmentManager.fragmentFactory.instantiate(context.classLoader,destination.className)
            fragmentTransaction.add(containerId,fragment,tag)
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNow()
        return destination
    }



}