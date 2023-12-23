package cn.lancet.navigation.func

import android.view.View
import androidx.appcompat.app.AppCompatActivity

class FuncActivity:AppCompatActivity(){



    fun func_01(view:View){
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                goPreview(v)
            }
        })
    }

    fun func_02(view: View){
        view.setOnClickListener(View.OnClickListener { v: View -> goPreview(v) })
    }

    fun func_03(view: View){
        view.setOnClickListener({v:View -> goPreview(v)})
    }

    fun func_04(view: View){
        view.setOnClickListener({v -> goPreview(v)})
    }

    fun func_05(view: View){
        view.setOnClickListener({ goPreview(it)})
    }



    private fun goPreview(view: View){}

}