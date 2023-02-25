package cn.lancet.navigation.util

import java.util.regex.Pattern

class CommonUtil {

    companion object {
        fun emailIsValid(email: String): Boolean{
            val emailRegex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"
            return Pattern.compile(emailRegex).matcher(email).matches()
        }
    }


}