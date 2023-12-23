package cn.lancet.navigation.select

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

class SelectV1 {

    fun mainV1() = runBlocking {

        suspend fun getCacheInfo(productId: String):Product? {
            delay(100)
            return Product(productId,9.9)
        }

        suspend fun getNetworkInfo(productId:String):Product? {
            delay(200)
            return Product(productId,9.8)
        }

        fun updateUI(product: Product){
            println("${product.productId} == ${product.price}")
        }

        val startTime = System.currentTimeMillis()

        val productId = "xxxId"

        val cacheInfo = getCacheInfo(productId)
        if (cacheInfo!=null){
            updateUI(cacheInfo)
            println("Time cost: ${System.currentTimeMillis() - startTime}")
        }

        val latestInfo = getNetworkInfo(productId)
        if (latestInfo!=null){
            updateUI(latestInfo)
            println("Time cost: ${System.currentTimeMillis() - startTime}")
        }

    }

    fun mainV2() = runBlocking {

        suspend fun getCacheInfo(productId: String):Product? {
            delay(100)
            return Product(productId,9.9)
        }

        suspend fun getNetworkInfo(productId:String):Product? {
            delay(200)
            return Product(productId,9.8)
        }

        fun updateUI(product: Product){
            println("${product.productId} == ${product.price}")
        }

        val startTime = System.currentTimeMillis()
        val productId = "xxxId"

        val cacheDeferred = async { getCacheInfo(productId) }
        val latestDeferred = async { getNetworkInfo(productId) }

        val product = select<Product?> {
            cacheDeferred.onAwait{
                    it?.copy(isCache = true)
                }

            latestDeferred.onAwait{
                    it?.copy(isCache = false)
                }
        }

        if (product!=null){
            updateUI(product)
            println("Time cost: ${System.currentTimeMillis() - startTime}")
        }

        if (product!=null && product.isCache){
            val latest = latestDeferred.await()?:return@runBlocking
            updateUI(latest)
            println("Time cost: ${System.currentTimeMillis() - startTime}")
        }

    }

}