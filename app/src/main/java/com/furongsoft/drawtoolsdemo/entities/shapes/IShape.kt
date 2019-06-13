package com.furongsoft.drawtoolsdemo.entities.shapes

import android.view.MotionEvent

interface IShape {
    fun onTouch(event: MotionEvent)
}