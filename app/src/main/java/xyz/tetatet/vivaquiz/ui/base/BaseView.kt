package  xyz.tetatet.vivaquiz.ui.base

import android.view.View
import androidx.annotation.StringRes


interface BaseView {
    fun handleException(throwable: Throwable?, action: Pair<String, () -> Unit>? = null)
    fun handleExceptionRes(throwable: Throwable?, action: Pair<Int, () -> Unit>? = null)
    fun handleExceptionRes(throwable: Throwable?, coordView: View?, action: Pair<Int, () -> Unit>?)
    fun showLoading()
    fun dismissLoading()
}