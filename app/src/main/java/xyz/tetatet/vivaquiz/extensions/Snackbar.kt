package xyz.tetatet.vivaquiz.extensions

import com.google.android.material.snackbar.Snackbar


internal fun Snackbar.dismissSnackBar() {
    if (isShownOrQueued) dismiss()
}
