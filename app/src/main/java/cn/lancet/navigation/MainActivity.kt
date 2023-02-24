package cn.lancet.navigation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commitNow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
//import cn.bmob.newim.BmobIM
//import cn.bmob.newim.bean.BmobIMUserInfo
//import cn.bmob.newim.listener.ConnectListener
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityMainBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.notice.PublishNoticeActivity
import cn.lancet.navigation.util.AppPreUtils
import cn.lancet.navigation.util.StatusBar
import coil.load
import com.hjq.toast.Toaster

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null

    private var mNavController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        getUserInfo()

        val avatar = AppPreUtils.getString(Constant.KEY_AVATAR)

        mBinding?.ivAvatar?.load(avatar){
            placeholder(R.mipmap.icon_default_avatar)
            error(R.mipmap.icon_default_avatar)
        }

        mBinding?.fabCreateNotice?.setOnClickListener {
            startActivity(Intent(this,PublishNoticeActivity::class.java))
        }

        val navHostFragment = NavHostFragment.create(R.navigation.lancet_navigation)

        supportFragmentManager.commitNow {
            this.replace(R.id.nav_host_fragment, navHostFragment)
                .setPrimaryNavigationFragment(navHostFragment)
        }


        mNavController = navHostFragment.navController

        mNavController?.navigatorProvider?.addNavigator(
            FixNavigator(this,supportFragmentManager,R.id.nav_host_fragment)
        )

        NavigationUI.setupWithNavController(mBinding!!.bottomNavigationView, mNavController!!)


    }


    private fun getUserInfo(){

        val currentUser = BmobUser.getCurrentUser()

        if (currentUser!=null && !currentUser.objectId.isNullOrBlank()){
//            BmobIM.connect(currentUser.objectId, object : ConnectListener() {
//                override fun done(uid: String?, e: BmobException?) {
//                    BmobIM.getInstance().updateUserInfo(
//                        BmobIMUserInfo()
//                    )
//                }
//            })
        }

//        val query = BmobQuery<User>()
//
//        query.getObject(AppPreUtils.getString(Constant.KEY_USER_ID),
//            object : QueryListener<User>() {
//                override fun done(user: User?, e: BmobException?) {
//                    if (e == null) {
//                        user?.let {
//                            AppPreUtils.putString(Constant.KEY_USER_NAME,it.name?:it.account?:"")
//                        }
//                    }
//                }
//            })

    }


}