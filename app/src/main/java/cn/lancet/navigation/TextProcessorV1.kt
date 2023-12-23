package cn.lancet.navigation

import java.io.File

class TextProcessorV1 {

    fun processText(text:String?=null,file:File?=null):List<WordFreq>{

        val list = mutableListOf<WordFreq>()

        file?.let {
            val readText = it.readText(Charsets.UTF_8)


        }

        return list

    }


    fun clean(text: String):String {
        return text.replace("[^A-Za-z]".toRegex()," ").trim()
    }

    fun getWordCount(list: List<String>):Map<String,Int>{
        val map = hashMapOf<String,Int>()
        for (word in list){
            if (word == "") continue
            val trim = word.trim()
            val count = map.getOrDefault(trim,0)
            map[trim] = count + 1
        }
        return map
    }

    fun sortByFrequency(map: Map<String,Int>):MutableList<WordFreq>{
        val list = mutableListOf<WordFreq>()
        for (entry in map){
            if (entry.key == "") continue
            val freq = WordFreq(entry.key,entry.value)
            list.add(freq)
        }
        list.sortByDescending {
            it.frequency
        }
        return list
    }

}




data class WordFreq(val world:String,val frequency:Int)









