package cn.lancet.navigation.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import cn.lancet.navigation.MainActivity
import cn.lancet.navigation.constans.Constant
import cn.lancet.navigation.databinding.ActivityLoginBinding
import cn.lancet.navigation.module.User
import cn.lancet.navigation.util.AppPreUtils


class LoginActivity:AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        initEvent()

    }

    private fun initEvent() {

        mBinding?.btnLogin?.setOnClickListener {

            val account = mBinding?.etAccount?.text.toString()
            if (account.isBlank()){
                Toast.makeText(this, "please input your account", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val password = mBinding?.etPwd?.text.toString()
            if (password.isBlank()){
                Toast.makeText(this, "please input your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(account,password)




        }
    }

    private fun login(account:String,pwd:String){
        val bmobQuery = BmobQuery<User>()

        var hasSignup = false

        bmobQuery.findObjects(object : FindListener<User>() {
            override fun done(users: MutableList<User>?, e: BmobException?) {
                if (e == null && !users.isNullOrEmpty()){
                    users.forEach {
                        if (it.account.equals(account)){
                            hasSignup = true
                            if (it.pwd.equals(pwd)){

                                it.objectId?.let {
                                    AppPreUtils.putString(Constant.KEY_USER_ID, it)
                                }

                                it.avatar?.let {
                                    AppPreUtils.putString(Constant.KEY_AVATAR,it)
                                }

//                                {
//                                    "code": 200,
//                                    "userId": "7efe36b075",
//                                    "token": "F1CGxX0eNJqy1DNMDN0eCamljoOYFl59QrGPAEc3930=@osi5.cn.rongnav.com;osi5.cn.rongcfg.com"
//                                }



                            }else{
                                Toast.makeText(this@LoginActivity, "The account and password don`t match.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    if (!hasSignup){
                        signUp(account,pwd)
                    }
                }
            }

        })

    }

    private fun signUp(account:String,password:String){
        val user = User(account = account, pwd = password,name = account)

        user.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
                if (e == null && !objectId.isNullOrBlank()){
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                }
            }

        })

    }


}