package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View

/**
 * 椭圆
 *
 * @author Alex
 */
class Ellipse(event: MotionEvent) : IShape(event) {
    override fun getVectors(): List<PointF> {
        return listOf()
    }

    override fun contains(event: MotionEvent): Boolean {
        return false
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        return true
    }

    override fun onDraw(canvas: Canvas, paint: Paint) {
    }

    override fun onCreate(view: View?, event: MotionEvent): Boolean {
        return true
    }

    override fun onPositionChanged(view: View?, offsetX: Float, offsetY: Float) {
    }

    override fun onControlBlockChanged(view: View?, block: ControlBlock) {
    }
}