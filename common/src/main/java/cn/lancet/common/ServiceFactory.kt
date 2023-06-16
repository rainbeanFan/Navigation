package cn.lancet.common

class ServiceFactory private constructor(){

    private var mIUserInstallService:IUserInstallService?=null

    fun getUserInstallService() = mIUserInstallService

    fun setUserInstallService(service: IUserInstallService){
        mIUserInstallService = service
    }

    private var mIUserExitService:IUserExitService?=null

    fun getUserExitService() = mIUserExitService

    fun setUserExitService(service: IUserExitService){
        mIUserExitService = service
    }

    companion object {

        val instance:ServiceFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            ServiceFactory()
        }

    }

}