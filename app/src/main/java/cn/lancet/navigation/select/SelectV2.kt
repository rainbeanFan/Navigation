package cn.lancet.navigation.select

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select

class SelectV2 {

    fun test(){
        main2()
    }

    fun main1() = runBlocking {
        val startTime = System.currentTimeMillis()

        val channel1 = produce {
            send(1)
            delay(200L)
            send(2)
            delay(200L)
            send(3)
            delay(150L)
        }

        val channel2 = produce {
            send("a")
            delay(200L)
            send("b")
            delay(200L)
            send("c")
            delay(150L)
        }

        channel1.consumeEach {
            println(it)
        }

        channel2.consumeEach {
            println(it)
        }

        println("Time cost:${System.currentTimeMillis() - startTime}")

    }

    fun main2() = runBlocking {
        val startTime = System.currentTimeMillis()

        val channel1 = produce {
            send("1")
            delay(200L)
            send("2")
            delay(200L)
            send("3")
            delay(150L)
        }

        val channel2 = produce {
            send("a")
            delay(200L)
            send("b")
            delay(200L)
            send("c")
            delay(150L)
        }

        suspend fun selectChannel(channel1:ReceiveChannel<String>,channel2: ReceiveChannel<String>):String = select {

            channel1.onReceive{
                it.also { println(it) }
            }

            channel2.onReceive{
                it.also { println(it) }
            }

        }

        repeat(6){
            selectChannel(channel1, channel2)
        }

        println("Time cost:${System.currentTimeMillis() - startTime}")

    }

}