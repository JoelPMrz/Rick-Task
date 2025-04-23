package com.example.ricktasks.core.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import com.example.ricktasks.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showNoConnectionDialog(context: Context, onRetry: () -> Unit) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.not_conection_dialog, null)

    val dialog = MaterialAlertDialogBuilder(context)
        .setView(dialogView)
        .setCancelable(false)
        .show()

    val btnRetry = dialogView.findViewById<Button>(R.id.btn_retry)
    btnRetry.setOnClickListener {
        if (!isNetworkAvailable(context)) {
            btnRetry.isEnabled = false
            btnRetry.postDelayed({
                btnRetry.isEnabled = true
            }, 2000)
        } else {
            dialog.dismiss()
            onRetry()
        }
    }
}
