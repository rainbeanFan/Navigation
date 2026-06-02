package cn.lancet.navigation.widget


import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout

/**
 * 胶囊形圆角裁剪容器。
 *
 * 用途：
 * 1. 强制把 BlurView / BottomNavigationView 裁成胶囊；
 * 2. 避免 BlurView 自己绘制矩形背景后突破圆角；
 * 3. 支持 elevation 阴影 outline。
 */
class PillClipFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val clipPath = Path()
    private val rectF = RectF()

    init {
        setWillNotDraw(false)
        clipChildren = true
        clipToPadding = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(
                        0,
                        0,
                        view.width,
                        view.height,
                        view.height / 2f
                    )
                }
            }
            clipToOutline = true
        }
    }

    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int
    ) {
        super.onSizeChanged(w, h, oldw, oldh)
        rebuildClipPath(w, h)
    }

    override fun draw(canvas: Canvas) {
        if (width <= 0 || height <= 0) {
            super.draw(canvas)
            return
        }

        val saveCount = canvas.save()
        canvas.clipPath(clipPath)
        super.draw(canvas)
        canvas.restoreToCount(saveCount)
    }

    private fun rebuildClipPath(width: Int, height: Int) {
        rectF.set(
            0f,
            0f,
            width.toFloat(),
            height.toFloat()
        )

        val radius = height / 2f

        clipPath.reset()
        clipPath.addRoundRect(
            rectF,
            radius,
            radius,
            Path.Direction.CW
        )
        clipPath.close()
    }
}