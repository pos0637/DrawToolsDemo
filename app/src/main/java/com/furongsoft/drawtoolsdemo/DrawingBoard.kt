package com.furongsoft.drawtoolsdemo

import android.graphics.Canvas
import android.view.MotionEvent
import com.furongsoft.drawtoolsdemo.entities.shapes.IShape
import java.util.*

class DrawingBoard {
    var shapes = LinkedList<IShape>()
    var currentShape: IShape? = null

    fun onTouch(event: MotionEvent) {
        if (currentShape != null) {
            currentShape?.onTouch(event)
        } else if (event.action == MotionEvent.ACTION_DOWN) {
            currentShape = shapes.first()
            currentShape?.onTouch(event)
        }
    }

    fun onDraw(canvas: Canvas) {

    }
}