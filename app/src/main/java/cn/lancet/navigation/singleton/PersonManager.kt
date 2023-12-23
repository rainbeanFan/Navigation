package cn.lancet.navigation.singleton

class PersonManager private constructor(name:String){
    companion object:BaseSingleton<String,PersonManager>() {
        override fun creator(params: String) =  PersonManager(params)

    }
}