package cn.lancet.navigation

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("cn.lancet.navigation", appContext.packageName)
    }

    fun main(vararg args:String){

        val nums = intArrayOf(2,7,11,15)
        val target = 9
        twoSum(nums, target)
    }

    fun twoSum(nums:IntArray,target:Int):IntArray{
        val intArray = mutableListOf<Int>()
        nums.forEachIndexed { index, i ->
            if (nums.contains(target - i) && index!=nums.indexOf(target - i)){
                intArray.add(index)
                intArray.add(nums.indexOf(target - i))
                return@forEachIndexed
            }
        }
        println(intArray)
        return intArray.toIntArray()
    }

}