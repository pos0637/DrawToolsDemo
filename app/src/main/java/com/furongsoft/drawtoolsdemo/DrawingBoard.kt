package com.furongsoft.drawtoolsdemo

import android.graphics.Canvas
import com.furongsoft.drawtoolsdemo.entities.shapes.IShape
import java.util.*

class DrawingBoard {
    var shapes = LinkedList<IShape>()
    var currentShape: IShape? = null

    fun onDraw(canvas: Canvas) {

    }
}