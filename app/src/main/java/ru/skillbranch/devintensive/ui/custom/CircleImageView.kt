package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private val DEFAULT_BORDER_COLOR = Color.WHITE
    private val DEFAULT_BORDER_WIDTH = 2

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = Utils.dpToPx(DEFAULT_BORDER_WIDTH)

    private var initials: String? = null

    private var bitmap: Bitmap? = null
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderCircle = RectF()

    init {
        if (attrs != null) {
            val attrVal = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = attrVal.getColor(R.styleable.CircleImageView_cv_borderColor, borderColor)
            borderWidth = attrVal.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, borderWidth)
            attrVal.recycle()
        }

        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth.toFloat()
    }

    fun getBorderWidth(): Int = Utils.pxToDp(borderWidth)

    fun setBorderWidth(dp: Int) {
        borderWidth = Utils.dpToPx(dp)
        invalidate()
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = context.getColor(colorId)
        invalidate()
    }

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        bitmap = getBitmapFromDrawable() ?: return

        scaleBitmap()
        cropBitmap()
        roundBitmap()
        borderBitmap()

        canvas.drawBitmap(bitmap!!, 0F, 0F, null)
    }

    private fun borderBitmap(){
        val borderStart = borderWidth / 2F
        val borderEnd = bitmap!!.width - borderWidth / 2F

        borderCircle.set(borderStart, borderStart, borderEnd, borderEnd)

        val canvas = Canvas(bitmap!!)
        canvas.drawOval(borderCircle, borderPaint)
    }

    private fun scaleBitmap() =
        if (bitmap!!.width != width || bitmap!!.height != width) {
            val minSize = min(bitmap!!.width, bitmap!!.height).toFloat()
            val coefScaling = minSize / width

            bitmap = Bitmap.createScaledBitmap(
                bitmap!!,
                (bitmap!!.width / coefScaling).toInt(),
                (bitmap!!.height / coefScaling).toInt(),
                false
            )
        } else {}

    private fun cropBitmap() {
        val cropStartX = (bitmap!!.width - width) / 2
        val cropStartY = (bitmap!!.height - height) / 2

        bitmap = Bitmap.createBitmap(bitmap!!, cropStartX, cropStartY, width, height)
    }

    private fun getBitmapFromDrawable(): Bitmap? {
        drawable ?: return null

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun roundBitmap() {
        val minSize = min(bitmap!!.width, bitmap!!.height)
        val circleBitmap = Bitmap.createBitmap(minSize, minSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(circleBitmap)

        val rect = Rect(0, 0, minSize, minSize)

        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(minSize / 2F, minSize / 2F, minSize / 2F, circlePaint)

        circlePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        canvas.drawBitmap(bitmap!!, rect, rect, circlePaint)

        bitmap = circleBitmap
    }

}