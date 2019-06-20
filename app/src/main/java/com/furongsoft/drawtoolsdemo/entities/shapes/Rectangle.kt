package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.PointF
import android.view.MotionEvent

/**
 * 矩形
 *
 * @author Alex
 */
class Rectangle : IShape {
    var point1 = PointF()
    var point2 = PointF()

    override fun onTouch(event: MotionEvent) {
    }
}