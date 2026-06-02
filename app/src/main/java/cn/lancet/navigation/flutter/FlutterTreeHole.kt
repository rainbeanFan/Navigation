package cn.lancet.navigation.flutter

import android.content.Context
import android.content.Intent
import android.net.Uri
import cn.lancet.navigation.module.Character

object FlutterTreeHole {

    private const val FLUTTER_ACTIVITY = "io.flutter.embedding.android.FlutterActivity"

    /**
     * 注意：
     * 这里不要使用 "/tree_hole"。
     *
     * Flutter 的 initialRoute 如果以 "/" 开头，可能会生成：
     * 1. /
     * 2. /tree_hole
     *
     * 如果 Flutter 端 "/" 页面也渲染 TreeHole，就会出现：
     * 点击返回后，先 pop 掉 /tree_hole，
     * 然后露出下面那个没有返回按钮的 TreeHole 页面。
     */
    private const val ROUTE = "tree_hole"

    private const val EXTRA_INITIAL_ROUTE = "route"
    private const val EXTRA_BACKGROUND_MODE = "background_mode"
    private const val EXTRA_DESTROY_ENGINE_WITH_ACTIVITY = "destroy_engine_with_activity"

    fun isTreeHole(character: Character): Boolean {
        return character.cnName == "树洞"
    }

    fun isAvailable(): Boolean {
        return runCatching {
            Class.forName(FLUTTER_ACTIVITY)
        }.isSuccess
    }

    fun createIntent(context: Context, character: Character): Intent {
        val route = Uri.Builder()
            .path(ROUTE)
            .appendQueryParameter("title", character.cnName)
            .appendQueryParameter("session", character.enName)
            .appendQueryParameter("prompt", character.prompt)
            .appendQueryParameter("welcome", character.welTips.orEmpty())
            .build()
            .toString()

        return Intent()
            .setClassName(context.packageName, FLUTTER_ACTIVITY)
            .putExtra(EXTRA_INITIAL_ROUTE, route)
            .putExtra(EXTRA_BACKGROUND_MODE, "opaque")
            .putExtra(EXTRA_DESTROY_ENGINE_WITH_ACTIVITY, true)
    }
}