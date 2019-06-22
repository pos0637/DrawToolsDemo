package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.furongsoft.misc.geometry.GeometryUtils

/**
 * 椭圆
 *
 * @author Alex
 */
class Ellipse(event: MotionEvent) : IShape(event) {
    private var point1 = PointF()
    private var point2 = PointF()
    private var dashPaint = Paint()

    init {
        point1 = PointF(event.x, event.y)
        point2 = PointF(event.x, event.y)
        dashPaint.style = Paint.Style.STROKE
        dashPaint.strokeWidth = 1f
        dashPaint.color = Color.BLACK
        dashPaint.pathEffect = DashPathEffect(floatArrayOf(1f, 1f), 0f)
        generateShape()
    }

    override fun getVectors(): List<PointF> {
        return listOf(point1, PointF(point2.x, point1.y), point2, PointF(point1.x, point2.y))
    }

    override fun contains(event: MotionEvent): Boolean {
        return GeometryUtils.isPointInPolygon(PointF(event.x, event.y), getVectors())
    }

    override fun onDraw(canvas: Canvas, paint: Paint) {
        canvas.drawOval(RectF(point1.x, point1.y, point2.x, point2.y), paint)
        if (status != Status.Initialized) {
            canvas.drawRect(RectF(point1.x, point1.y, point2.x, point2.y), dashPaint)
            onDrawControlBlocks(canvas, paint)
        }
    }

    override fun onCreate(view: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                point2 = PointF(event.x, event.y)
                generateShape()
                return true
            }
            MotionEvent.ACTION_UP -> {
                status = Status.Initialized
                return false
            }
            else -> return false
        }
    }

    override fun onPositionChanged(view: View?, offsetX: Float, offsetY: Float) {
        if (((point1.x + offsetX) < 0)
            || ((point1.y + offsetY) < 0)
            || ((point2.x + offsetX) < 0)
            || ((point2.y + offsetY) < 0)
        )
            return

        if ((view != null)
            && (((point1.x + offsetX) >= view.width)
                    || ((point1.y + offsetY) >= view.height)
                    || ((point2.x + offsetX) >= view.width)
                    || ((point2.y + offsetY) >= view.height))
        )
            return

        point1.offset(offsetX, offsetY)
        point2.offset(offsetX, offsetY)
        shapeChanged = true
    }

    override fun onControlBlockChanged(view: View?, block: ControlBlock) {
        when (block.id) {
            0 -> {
                point1.x = block.centerX()
                point1.y = block.centerY()
            }
            1 -> {
                point2.x = block.centerX()
                point1.y = block.centerY()
            }
            2 -> {
                point2.x = block.centerX()
                point2.y = block.centerY()
            }
            3 -> {
                point1.x = block.centerX()
                point2.y = block.centerY()
            }
            4 -> {
                point1.x = block.centerX()
            }
            5 -> {
                point1.y = block.centerY()
            }
            6 -> {
                point2.x = block.centerX()
            }
            7 -> {
                point2.y = block.centerY()
            }
        }

        shapeChanged = true
    }

    override fun generateShape() {
        blocks.clear()
        blocks.add(ControlBlock(0, point1.x, point1.y))
        blocks.add(ControlBlock(1, point2.x, point1.y))
        blocks.add(ControlBlock(2, point2.x, point2.y))
        blocks.add(ControlBlock(3, point1.x, point2.y))
        blocks.add(ControlBlock(4, point1.x, point1.y + (point2.y - point1.y) / 2))
        blocks.add(ControlBlock(5, point1.x + (point2.x - point1.x) / 2, point1.y))
        blocks.add(ControlBlock(6, point2.x, point1.y + (point2.y - point1.y) / 2))
        blocks.add(ControlBlock(7, point1.x + (point2.x - point1.x) / 2, point2.y))

        super.generateShape()
    }
}