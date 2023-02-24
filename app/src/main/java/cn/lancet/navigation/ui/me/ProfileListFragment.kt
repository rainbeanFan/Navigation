package cn.lancet.navigation.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ProfileListFragment: Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }




    companion object {

        fun newInstance(tabType:String): ProfileListFragment{
            val args = Bundle().apply {
                putString("TYPE_TAB",tabType)
            }
            return ProfileListFragment().apply {
                arguments = args
            }
        }

    }

}