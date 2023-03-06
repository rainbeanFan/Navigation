package cn.lancet.navigation


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FetchUserInfoListener
import cn.bmob.v3.listener.QueryListener
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityMainBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.notice.PublishNoticeActivity
import cn.lancet.navigation.util.AppPreUtils
import coil.load
import com.hjq.toast.Toaster
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient

class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null

    private var mNavController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        getUserInfo()

        mBinding?.fabCreateNotice?.setOnClickListener {
            startActivity(Intent(this, PublishNoticeActivity::class.java))
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

        if (BmobUser.isLogin()){
            BmobUser.fetchUserInfo(object : FetchUserInfoListener<User>() {
                override fun done(user: User?, e: BmobException?) {
                    if (e == null){
                        val currentUser = BmobUser.getCurrentUser(User::class.java)
                    }
                }

            })
        }

        BmobQuery<User>().getObject(AppPreUtils.getString(Constant.KEY_USER_ID),
            object : QueryListener<User>() {
                override fun done(user: User?, e: BmobException?) {
                    if (e == null) {
                        user?.let {
                            AppPreUtils.putString(Constant.KEY_USER_NAME,it.name?:it.account?:"")
                            connectRC(it.RCToken)
                        }
                    }
                }
            })

    }


    private fun connectRC(RCToken: String?){

        if (RCToken.isNullOrBlank()){
            Toaster.showLong("连接失败！")
            return
        }

        RongIM.connect(RCToken, object : RongIMClient.ConnectCallback() {
            override fun onSuccess(t: String?) {
                Toaster.showLong("IM 连接成功!")
            }

            override fun onError(e: RongIMClient.ConnectionErrorCode?) {
                Toaster.showLong("IM 连接失败 ${e.toString()}!")
            }

            override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) {
                Toaster.showLong("IM 连接成功 ${code.toString()}!")
            }

        })
    }

}