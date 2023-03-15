package cn.lancet.navigation

import androidx.room.util.copy
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println("start test!")

//        val nums = intArrayOf(-1,-2,-3,-4,-5)
//        val target = -8
//        twoSum(nums, target)

        val nums1 = intArrayOf(1,2,3,0,0,0)
        val m = 3
        val num2 = intArrayOf(2,5,6)
        val n = 3

        merge(nums1,m,num2,n)

        assertEquals(4, 2 + 2)
    }

    fun twoSum(nums:IntArray,target:Int):IntArray{
        val intArray = mutableSetOf<Int>()
        val intMap = mutableMapOf<Int,Int>()
        nums.forEachIndexed { index, i ->
            intMap.put(index,i)
        }
        for (i in 0 until intMap.size){
            for (j in i+1 until intMap.size){
                if (target - intMap[i]!! == intMap[j]!!){
                    intArray.add(i)
                    intArray.add(j)
                }
            }
        }
        println(intArray)
        return intArray.toIntArray()
    }

    fun merge(nums1: IntArray, m: Int, nums2: IntArray, n: Int) {
        val copy = IntArray(m)
        for (i in 0 until m){
            copy[i] = nums1[i]
        }

        var left = 0
        var right = 0

        for (i in 0 until m+n){
            nums1[i] = if (left>=m){
                nums2[right++]
            }else if (right>=n){
                copy[left++]
            }else if (copy[left]<nums2[right]){
                copy[left++]
            }else{
                nums2[right++]
            }
        }

        nums1.forEach {
            println(it)
        }



    }

}