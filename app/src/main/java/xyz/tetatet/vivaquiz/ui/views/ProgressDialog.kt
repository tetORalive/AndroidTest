package xyz.tetatet.vivaquiz.ui.views

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.dialog_progress.*
import xyz.tetatet.vivaquiz.R

class ProgressDialog(context: Context, private var message: String? = null) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.transparent)))
        loadingProgressBar.visibility =  View.VISIBLE
        message?.let {
            progressMessage.apply {
                text = it
                isVisible = true
            }
        }
    }
}