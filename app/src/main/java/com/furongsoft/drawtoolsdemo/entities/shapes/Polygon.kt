package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import com.furongsoft.misc.geometry.GeometryUtils
import java.util.*

/**
 * 多边形
 *
 * @author Alex
 */
class Polygon(event: MotionEvent) : IShape(event) {
    private var points = LinkedList<PointF>()
    private var firstBlock: ControlBlock
    private var path = Path()

    init {
        points.add(PointF(event.x, event.y))
        firstBlock = ControlBlock(0, event.x, event.y)
        generateShape()
    }

    override fun getVectors(): List<PointF> {
        return points
    }

    override fun contains(event: MotionEvent): Boolean {
        return GeometryUtils.isPointInPolygon(PointF(event.x, event.y), getVectors())
    }

    override fun onDraw(canvas: Canvas, paint: Paint) {
        canvas.drawPath(path, paint)
        if (status != Status.Initialized) onDrawControlBlocks(canvas, paint)
    }

    override fun onCreate(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                points.add(PointF(event.x, event.y))
                generateShape()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                points.removeLast()
                points.add(PointF(event.x, event.y))
                generateShape()
                return true
            }
            MotionEvent.ACTION_UP -> {
                if ((points.size > 2) && firstBlock.contains(event)) {
                    points.removeLast()
                    status = Status.Initialized
                    return false
                }

                points.removeLast()
                points.add(PointF(event.x, event.y))
                generateShape()
                return true
            }
            else -> return false
        }
    }

    override fun onPositionChanged(view: View?, offsetX: Float, offsetY: Float) {
        for (point in points) {
            if (((point.x + offsetX) < 0)
                || ((point.y + offsetY) < 0)
                || ((point.x + offsetX) < 0)
                || ((point.y + offsetY) < 0)
            )
                return

            if ((view != null)
                && (((point.x + offsetX) >= view.width)
                        || ((point.y + offsetY) >= view.height)
                        || ((point.x + offsetX) >= view.width)
                        || ((point.y + offsetY) >= view.height))
            )
                return
        }

        points.forEach { point -> point.offset(offsetX, offsetY) }
        shapeChanged = true
    }

    override fun onControlBlockChanged(view: View?, block: ControlBlock) {
        val point = points[block.id]
        point.x = block.centerX()
        point.y = block.centerY()
        shapeChanged = true
    }

    override fun generateShape() {
        path.reset()
        path.moveTo(points[0].x, points[0].y)
        for (id in 1 until points.size) {
            path.lineTo(points[id].x, points[id].y)
        }
        if (status != Status.Created) path.close()

        blocks.clear()
        for ((id, point) in points.withIndex()) {
            blocks.add(ControlBlock(id, point.x, point.y))
        }

        super.generateShape()
    }
}