package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent

/**
 * 椭圆
 *
 * @author Alex
 */
class Ellipse(event: MotionEvent) : IShape(event) {
    override fun getVectors(): List<PointF> {
        return listOf()
    }

    override fun onTouch(event: MotionEvent): Boolean {
        return true
    }

    override fun onDraw(canvas: Canvas, paint: Paint) {
    }
}