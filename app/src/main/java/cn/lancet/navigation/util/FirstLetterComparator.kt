package cn.lancet.navigation.util

import cn.lancet.navigation.module.User

class FirstLetterComparator : Comparator<User> {
    override fun compare(o1: User?, o2: User?): Int {
        if (o1?.sort_letter.equals("@") || o2?.sort_letter.equals("#")) {
            return -1
        } else if (o1?.sort_letter.equals("#") || o2?.sort_letter.equals("@")) {
            return 1
        } else {
            return o1?.sort_letter!!.compareTo(o2?.sort_letter!!)
        }
    }

}