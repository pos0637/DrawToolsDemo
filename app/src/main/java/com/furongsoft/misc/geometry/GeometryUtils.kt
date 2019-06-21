package com.furongsoft.misc.geometry

import android.graphics.PointF

/**
 * 几何工具
 *
 * @author Alex
 */
object GeometryUtils {
    /**
     * 判断点是否在多边形中
     */
    fun isPointInPolygon(point: PointF, polygon: List<PointF>): Boolean {
        val px = point.x
        val py = point.y
        var flag = false

        var i = 0
        var j = polygon.size - 1

        while (i < polygon.size) {
            val sx = polygon[i].x
            val sy = polygon[i].y
            val tx = polygon[j].x
            val ty = polygon[j].y

            // 点与多边形顶点重合
            if (((sx == px) && (sy == py)) || ((tx == px) && (ty == py))) {
                return true
            }

            // 判断线段两端点是否在射线两侧
            if (((sy < py) && (ty >= py)) || ((sy >= py) && (ty < py))) {
                // 线段上与射线 Y 坐标相同的点的 X 坐标
                val x = sx + (py - sy) * (tx - sx) / (ty - sy)

                // 点在多边形的边上
                if (x == px) {
                    return true
                }

                // 射线穿过多边形的边界
                if (x > px) {
                    flag = !flag
                }
            }

            j = i
            i++
        }

        return flag
    }
}