package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.*
import android.view.MotionEvent
import android.view.View

/**
 * 控制块
 *
 * @author alex
 */
class ControlBlock(
    id: Int,
    centerX: Float,
    centerY: Float,
    touchSize: Int = 5,
    paintSize: Int = 3
) : IShape(null) {
    /**
     * 索引
     */
    var id = 0

    /**
     * 触控区域
     */
    private var rectTouch: RectF

    /**
     * 绘图区域
     */
    private var rectPaint: RectF

    /**
     * 画笔
     */
    private var paint = Paint()

    init {
        this.id = id
        rectTouch = RectF(centerX - touchSize, centerY - touchSize, centerX + touchSize, centerY + touchSize)
        rectPaint = RectF(centerX - paintSize, centerY - paintSize, centerX + paintSize, centerY + paintSize)
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
    }

    override fun getVectors(): List<PointF> {
        TODO("not implemented") //To change body of Created functions use File | Settings | File Templates.
    }

    override fun contains(event: MotionEvent): Boolean {
        return rectTouch.contains(event.x, event.y)
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        TODO("not implemented") //To change body of Created functions use File | Settings | File Templates.
    }

    override fun onDraw(canvas: Canvas, paint: Paint) {
        canvas.drawRect(rectPaint, this.paint)
    }

    override fun onCreate(view: View?, event: MotionEvent): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPositionChanged(view: View?, offsetX: Float, offsetY: Float) {
        rectTouch.offset(offsetX, offsetY)
        rectPaint.offset(offsetX, offsetY)
    }

    override fun onControlBlockChanged(view: View?, block: ControlBlock) {
        TODO("not implemented") //To change body of Created functions use File | Settings | File Templates.
    }

    fun centerX(): Float {
        return rectTouch.centerX()
    }

    fun centerY(): Float {
        return rectTouch.centerY()
    }
}