package xyz.tetatet.vivaquiz.controllers.messages

import android.app.Activity
import android.content.DialogInterface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.extensions.dismissSnackBar
import xyz.tetatet.vivaquiz.extensions.getColorFromAttr
import javax.inject.Singleton


@Singleton
open class MessagesController(val activity: Activity? = null) {

    /*Constants ------------------------------------------------------------------------*/
    companion object;

    /*Fields ---------------------------------------------------------------------------*/
    private var snackBar: Snackbar? = null

    /*Initialization -------------------------------------------------------------------*/
    init {
    }

    /*Title ---------------------------------- Snack Bar ----------------------------------*/


    fun showSnackBarMessage(
        message: CharSequence,
        action: Pair<String, () -> Unit>? = null
    ) {
        showSnackBarMessage(
            message.toString(),
            if (action != null) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG,
            null,
            action
        )
    }

    fun showSnackBarMessage(
        message: CharSequence,
        snackbarParent: View?,
        action: Pair<String, () -> Unit>? = null
    ) {
        showSnackBarMessage(
            message.toString(),
            if (action != null) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG,
            snackbarParent,
            action
        )
    }

    fun showSnackBarMessage(
        message: String?,
        snackBarDuration: Int,
        snackbarParent: View?,
        action: Pair<String, () -> Unit>? = null,
        fromTop: Boolean = false
    ) {

        if (!message.isNullOrBlank()) {
            snackBar?.dismissSnackBar()
            activity?.let {
                snackBar = Snackbar.make(
                    snackbarParent ?: it.findViewById(R.id.snackbar_coordinator),
                    message,
                    snackBarDuration
                ).apply {
                    view.apply {
                        customizableSnackBar(
                            alignment = View.TEXT_ALIGNMENT_VIEW_START,
                            top = fromTop
                        )
                        action(it, action)
                    }
                    show()
                }
            }
        }
    }

    private fun Snackbar.customizableSnackBar(alignment: Int = View.TEXT_ALIGNMENT_CENTER, top: Boolean = false) {
        activity?.let {
            apply {
                view.apply {
                    setBackgroundTint(it.getColorFromAttr(R.attr.colorError))
                    (findViewById(R.id.snackbar_text) as? TextView)
                        ?.apply {
                            textAlignment = alignment
                            setTextColor(it.getColorFromAttr(R.attr.colorOnSurface))
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                it.resources.getDimension(R.dimen.text_small)
                            )
                        }
                    //Comments -> Adjust Snackbar height for fullscreen immersive mode
                    val params = view.layoutParams as CoordinatorLayout.LayoutParams
                    params.setMargins(0, 0, 0, 0)
                    if (top) params.gravity = Gravity.TOP
                    view.layoutParams = params
                }
            }
        }
    }

    private fun Snackbar.action(activity: Activity, action: Pair<String, () -> Unit>? = null) {
        if (action != null) {
            setActionTextColor(activity.getColorFromAttr(R.attr.colorOnSurface))
            setAction(action.first.toUpperCase()) {
                action.second()
                dismiss()
            }
            view.apply {
                setOnClickListener { dismissSnackBar() }
            }
        }
    }


    fun dismiss() {
        snackBar?.dismissSnackBar()
    }
}