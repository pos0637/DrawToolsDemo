package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.*
import android.view.MotionEvent
import java.util.*

/**
 * 矩形
 *
 * @author Alex
 */
class Rectangle(event: MotionEvent) : IShape(event) {
    val blockSize = 3f
    var point1 = PointF()
    var point2 = PointF()
    var path = Path()
    var blocks = LinkedList<RectF>()
    var blackPaint = Paint()

    init {
        point1 = PointF(event.x, event.y)
        point2 = PointF(event.x, event.y)
        blackPaint.color = Color.BLACK
        blackPaint.style = Paint.Style.FILL
    }

    override fun getVectors(): List<PointF> {
        return listOf(point1, PointF(point2.x, point1.y), point2, PointF(point1.x, point2.y))
    }

    override fun onTouch(event: MotionEvent): Boolean {
        when (status) {
            Status.created -> {
                if (event.action == MotionEvent.ACTION_MOVE) {
                    point2 = PointF(event.x, event.y)
                    generatePath()
                } else if (event.action == MotionEvent.ACTION_UP) {
                    status = Status.initialized
                    return false
                }
            }
            Status.initialized -> {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    status = Status.editing
                }
            }
            Status.editing -> {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    status = Status.editing
                }
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas, paint: Paint) {
        canvas.drawPath(path, paint)
        if (status != Status.initialized) blocks.forEach { block -> canvas.drawRect(block, blackPaint) }
    }

    /**
     * 生成路径
     */
    private fun generatePath() {
        path.reset()
        path.moveTo(point1.x, point1.y)
        path.lineTo(point1.x, point2.y)
        path.lineTo(point2.x, point2.y)
        path.lineTo(point2.x, point1.y)
        path.lineTo(point1.x, point1.y)

        blocks.clear()
        blocks.add(RectF(point1.x - blockSize, point1.y - blockSize, point1.x + blockSize, point1.y + blockSize))
        blocks.add(RectF(point2.x - blockSize, point1.y - blockSize, point2.x + blockSize, point1.y + blockSize))
        blocks.add(RectF(point2.x - blockSize, point2.y - blockSize, point2.x + blockSize, point2.y + blockSize))
        blocks.add(RectF(point1.x - blockSize, point2.y - blockSize, point1.x + blockSize, point2.y + blockSize))
    }

    private fun checkPointInBlock(event: MotionEvent): RectF? {
        for (block in blocks) {
            if (block.contains(event.x, event.y)) return block
        }

        return null
    }
}