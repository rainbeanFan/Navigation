package cn.lancet.common

class ServiceFactory private constructor(){

    private var mIUserInstallService:IUserInstallService?=null

    fun getUserInstallService() = mIUserInstallService

    fun setUserInstallService(service: IUserInstallService){
        mIUserInstallService = service
    }

    companion object {

        val instance:ServiceFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            ServiceFactory()
        }

    }

}