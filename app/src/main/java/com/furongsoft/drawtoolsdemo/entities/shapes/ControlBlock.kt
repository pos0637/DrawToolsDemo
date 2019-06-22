package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.*
import android.view.MotionEvent

/**
 * 控制块
 *
 * @author alex
 */
class ControlBlock(
    id: Int,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    onTouchListener: (block: ControlBlock, event: MotionEvent) -> Unit
) : IShape(null) {
    /**
     * 索引
     */
    var id = 0

    /**
     * 区域
     */
    var rect: RectF

    /**
     * 画笔
     */
    var paint = Paint()

    /**
     * 触屏事件处理函数
     */
    var onTouchListener: (block: ControlBlock, event: MotionEvent) -> Unit

    init {
        this.id = id
        this.onTouchListener = onTouchListener
        rect = RectF(left, top, right, bottom)
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
    }

    override fun getVectors(): List<PointF> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTouch(event: MotionEvent): Boolean {
        onTouchListener(this, event)
        return true
    }

    override fun onDraw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(rect, this.paint)
    }

    fun contains(event: MotionEvent): Boolean {
        return rect.contains(event.x, event.y)
    }

    fun offset(x: Float, y: Float) {
        rect.offset(x, y)
    }

    fun centerX(): Float {
        return rect.centerX()
    }

    fun centerY(): Float {
        return rect.centerY()
    }
}