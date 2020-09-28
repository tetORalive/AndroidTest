package xyz.tetatet.vivaquiz.extensions

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}


fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Context?.isKeyboardOpen(): Boolean {
    var view = (this as? AppCompatActivity)?.currentFocus
    if (view == null) view = View(this)
    val imm = this?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return false
    return imm.isAcceptingText
}