package com.furongsoft.drawtoolsdemo.entities.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent

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
        created,

        /**
         * 已初始化
         */
        initialized,

        /**
         * 编辑中
         */
        editing
    }

    /**
     * 状态
     */
    var status: Status = Status.created

    /**
     * 获取顶点
     */
    abstract fun getVectors(): List<PointF>

    /**
     * 触屏事件
     *
     * @return 是否响应该形状触屏事件
     */
    abstract fun onTouch(event: MotionEvent): Boolean

    /**
     * 绘图事件
     */
    abstract fun onDraw(canvas: Canvas, paint: Paint)
}