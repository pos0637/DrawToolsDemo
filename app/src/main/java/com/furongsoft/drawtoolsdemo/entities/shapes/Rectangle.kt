package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.view.MotionEvent
import java.util.*

/**
 * 矩形
 *
 * @author Alex
 */
class Rectangle(event: MotionEvent) : IShape(event) {
    private val blockSize = 3f
    private var point1 = PointF()
    private var point2 = PointF()
    private var path = Path()
    private var blocks = LinkedList<ControlBlock>()
    private var selectedBlockId: Int = -1
    private var selectedBlockPoint: PointF? = null
    private var pathChanged = false

    init {
        point1 = PointF(event.x, event.y)
        point2 = PointF(event.x, event.y)
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
                blocks.forEach { block -> block.onTouch(event) }
                if (pathChanged) generatePath()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas, paint: Paint) {
        canvas.drawPath(path, paint)
        if (status != Status.initialized) blocks.forEach { block -> block.onDraw(canvas, paint) }
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
        blocks.add(
            ControlBlock(
                0,
                point1.x - blockSize,
                point1.y - blockSize,
                point1.x + blockSize,
                point1.y + blockSize
            ) { block, event -> onTouchListener(block, event) }
        )
        blocks.add(
            ControlBlock(
                1,
                point2.x - blockSize,
                point1.y - blockSize,
                point2.x + blockSize,
                point1.y + blockSize
            ) { block, event -> onTouchListener(block, event) }
        )
        blocks.add(
            ControlBlock(
                2,
                point2.x - blockSize,
                point2.y - blockSize,
                point2.x + blockSize,
                point2.y + blockSize
            ) { block, event -> onTouchListener(block, event) }
        )
        blocks.add(
            ControlBlock(
                3,
                point1.x - blockSize,
                point2.y - blockSize,
                point1.x + blockSize,
                point2.y + blockSize
            ) { block, event -> onTouchListener(block, event) }
        )

        pathChanged = false
    }

    private fun onTouchListener(block: ControlBlock, event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (block.contains(event)) {
                selectedBlockId = block.id
                selectedBlockPoint = PointF(event.x, event.y)
            }
            return
        } else if (event.action == MotionEvent.ACTION_UP) {
            if (block.id == selectedBlockId) selectedBlockId = -1
            return
        } else if (event.action != MotionEvent.ACTION_MOVE) {
            return
        } else if (block.id != selectedBlockId) {
            return
        }

        block.offset(event.x - selectedBlockPoint!!.x, event.y - selectedBlockPoint!!.y)
        selectedBlockPoint = PointF(event.x, event.y)
        pathChanged = true

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
        }
    }
}