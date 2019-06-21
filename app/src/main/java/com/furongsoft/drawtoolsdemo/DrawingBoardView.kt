package com.furongsoft.drawtoolsdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.furongsoft.drawtoolsdemo.entities.shapes.IShape
import com.furongsoft.drawtoolsdemo.entities.shapes.Rectangle
import com.furongsoft.misc.geometry.GeometryUtils
import java.lang.reflect.Constructor
import java.util.*

/**
 * 绘图板视图
 */
class DrawingBoardView : View, View.OnTouchListener {
    /**
     * 形状列表
     */
    var shapes = LinkedList<IShape>()

    /**
     * 形状类型
     */
    var shapeType: Class<*>? = Rectangle::class.java

    /**
     * 当前操作形状
     */
    var currentShape: IShape? = null

    /**
     * 选中形状
     */
    var selectedShape: IShape? = null

    /**
     * 画笔
     */
    var paint: Paint = Paint()

    constructor(context: Context?) : super(context) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initialize()
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (currentShape != null) {
            onTouchShape(event)
        } else if (event?.action == MotionEvent.ACTION_DOWN) {
            currentShape = shapes.firstOrNull { shape -> isPointInPolygon(event, shape) }
            if (currentShape != null) {
                onTouchShape(event)
            } else if (shapeType != null) {
                onCreateShape(event)
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        shapes.forEach { shape -> shape.onDraw(canvas!!, paint) }
    }

    private fun initialize() {
        setOnTouchListener(this)

        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
    }

    private fun onTouchShape(event: MotionEvent?) {
        if (!currentShape!!.onTouch(event!!)) {
            currentShape = null
        }

        invalidate()
    }

    private fun onCreateShape(event: MotionEvent?) {
        val ctor: Constructor<*>? = shapeType?.getDeclaredConstructor(MotionEvent::class.java)
        currentShape = ctor?.newInstance(event!!) as IShape
        shapes.addFirst(currentShape)
        invalidate()
    }

    private fun isPointInPolygon(event: MotionEvent, shape: IShape): Boolean {
        return GeometryUtils.isPointInPolygon(PointF(event.x, event.y), shape.getVectors())
    }
}