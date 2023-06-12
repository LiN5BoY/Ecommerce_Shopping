package com.example.ecommerce_shopping.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// Android RecyclerView.ItemDecoration 是用于在 RecyclerView 中绘制 item 的自定义 Decoration。
// 它可以在 RecyclerView 中增加额外的绘制效果，如阴影、圆形、边框等，从而使 RecyclerView 更加美观和吸引人。
class HorizontalItemDecoration(private val amount : Int = 15) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // 在 RecyclerView 中，getItemOffsets() 方法用于返回 item 在屏幕上的偏移量。
        // getItemOffsets() 方法返回的 Rect 对象表示 item 在屏幕上的矩形区域。
        // 该矩形区域的偏移量是由 RecyclerView 计算出来的，用于调整 item 的大小，使其在屏幕上显示正确。
        outRect.right = amount
    }

}