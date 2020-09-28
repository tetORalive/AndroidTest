package xyz.tetatet.vivaquiz.extensions

import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.drawerlayout.widget.DrawerLayout

fun View.setFullWidth(windowManager:WindowManager) {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    this.apply {
        (layoutParams as DrawerLayout.LayoutParams).width = displayMetrics.widthPixels
    }
}