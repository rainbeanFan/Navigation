package cn.lancet.navigation.singleton

abstract class BaseSingleton<in P,out T> {

    @Volatile
    private var instance:T? = null

    protected abstract fun creator(params:P):T

    fun getInstance(params:P):T = instance ?: synchronized(this){
        instance ?: creator(params).also { instance = it }
    }

}