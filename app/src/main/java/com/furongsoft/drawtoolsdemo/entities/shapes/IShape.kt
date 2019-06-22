package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 * 形状
 *
 * @author Alex
 */
abstract class IShape(event: MotionEvent?) {
    /**
     * 状态
     */
    enum class Status {
        /**
         * 已创建
         */
        Created,

        /**
         * 已初始化
         */
        Initialized,

        /**
         * 编辑中
         */
        Editing
    }

    /**
     * 状态
     */
    var status: Status = Status.Created

    /**
     * 形状改变标志
     */
    protected var shapeChanged = false

    /**
     * 控制块列表
     */
    protected var blocks = LinkedList<ControlBlock>()

    /**
     * 选中控制块索引
     */
    private var selectedBlockId = -1

    /**
     * 选中控制块位置
     */
    private var selectedBlockPoint: PointF? = null

    /**
     * 选中形状标识
     */
    private var selectedShape = false

    /**
     * 选中形状位置
     */
    private var selectedShapePoint: PointF? = null

    /**
     * 获取顶点
     */
    abstract fun getVectors(): List<PointF>

    /**
     * 判断点是否在形状中
     */
    abstract fun contains(event: MotionEvent): Boolean

    /**
     * 绘图事件
     */
    abstract fun onDraw(canvas: Canvas, paint: Paint)

    /**
     * 创建形状事件
     *
     * @return 是否响应该形状触屏事件
     */
    abstract fun onCreate(view: View?, event: MotionEvent): Boolean

    /**
     * 位置改变事件
     */
    abstract fun onPositionChanged(view: View?, offsetX: Float, offsetY: Float)

    /**
     * 控制块改变事件
     */
    abstract fun onControlBlockChanged(view: View?, block: ControlBlock)

    /**
     * 触屏事件
     *
     * @return 是否响应该形状触屏事件
     */
    open fun onTouch(view: View?, event: MotionEvent): Boolean {
        when (status) {
            Status.Created -> return onCreate(view, event)
            Status.Initialized -> {
                if (!contains(event)) return false
                if (event.action == MotionEvent.ACTION_DOWN) {
                    status = Status.Editing
                    return onTouch(view, event)
                }
            }
            Status.Editing -> {
                if (!onTouchControlBlocks(view, event)) {
                    if (!onTouchShape(view, event)) {
                        status = Status.Initialized
                        return false
                    }
                }

                if (shapeChanged) generateShape()
                return true
            }
        }

        return false
    }

    /**
     * 形状触屏事件
     *
     * @return 是否处理该事件
     */
    protected fun onTouchShape(view: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (!contains(event)) return false
            selectedShape = true
            selectedShapePoint = PointF(event.x, event.y)
        } else if (event.action == MotionEvent.ACTION_UP) {
            selectedShape = false
            selectedShapePoint = null
        } else if (selectedShape && (event.action == MotionEvent.ACTION_MOVE)) {
            onPositionChanged(view, event.x - selectedShapePoint!!.x, event.y - selectedShapePoint!!.y)
            selectedShapePoint = PointF(event.x, event.y)
        }

        return true
    }

    /**
     * 控制块触屏事件
     *
     * @return 是否处理该事件
     */
    protected fun onTouchControlBlocks(view: View?, event: MotionEvent): Boolean {
        var handle = false
        blocks.forEach { block -> if (onTouchControlBlock(view, block, event)) handle = true }
        return handle
    }

    /**
     * 控制块绘图事件
     */
    protected fun onDrawControlBlocks(canvas: Canvas, paint: Paint) {
        blocks.forEach { block -> block.onDraw(canvas, paint) }
    }

    /**
     * 生成形状事件
     */
    protected open fun generateShape() {
        shapeChanged = false
    }

    /**
     * 控制块触屏事件
     *
     * @return 是否处理该事件
     */
    private fun onTouchControlBlock(view: View?, block: ControlBlock, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!block.contains(event)) return false
                selectedBlockId = block.id
                selectedBlockPoint = PointF(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (block.id == selectedBlockId) selectedBlockId = -1
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                if (block.id != selectedBlockId) return false
                block.onPositionChanged(view, event.x - selectedBlockPoint!!.x, event.y - selectedBlockPoint!!.y)
                selectedBlockPoint = PointF(event.x, event.y)
                onControlBlockChanged(view, block)
                return true
            }
            else -> return false
        }
    }
}