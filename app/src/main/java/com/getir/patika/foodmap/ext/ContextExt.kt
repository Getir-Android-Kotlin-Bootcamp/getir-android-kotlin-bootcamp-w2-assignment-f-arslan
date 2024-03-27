package com.getir.patika.foodmap.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.getir.patika.foodmap.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Opens a dialog that provides a rationale for requiring settings access and directs the user to the app's settings screen.
 *
 * This dialog is intended to be shown when the user has denied a permission and may need to enable it manually
 * through the app's settings page. The dialog includes a positive button that will take the user directly to the
 * settings page for this application.
 *
 * @receiver Context The context in which the dialog should be displayed.
 */
fun Context.openSettingsRationaleDialog() {
    MaterialAlertDialogBuilder(this)
        .setTitle(getString(R.string.location_dialog_title))
        .setMessage(getString(R.string.location_dialog_message))
        .setIcon(R.drawable.info)
        .setPositiveButton(getString(R.string.open_settings)) { dialog, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            dialog.dismiss()
            startActivity(intent)
        }
        .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}

/**
 * Shows a greeting dialog with a custom message and an icon.
 *
 * The dialog serves as a simple greeting to the user and includes a positive button that, when clicked,
 * executes a custom action defined by the caller of the function.
 *
 * @receiver Context The context in which the dialog should be displayed.
 * @param onPositiveButtonClick A lambda expression that will be invoked when the positive button is clicked.
 */
fun Context.greetingRationaleDialog(onPositiveButtonClick: () -> Unit) {
    val messageTextView = TextView(this).apply {
        setText(R.string.greeting_dialog_message)
        textSize = 16f
        setPadding(32, 32, 32, 0)
    }

    MaterialAlertDialogBuilder(this)
        .setTitle(getString(R.string.greeting_dialog_title))
        .setView(messageTextView)
        .setIcon(R.drawable.waving_hand)
        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            onPositiveButtonClick()
            dialog.dismiss()
        }
        .create()
        .show()
}


fun Context.makeToast(@StringRes textId: Int) {
    Toast.makeText(this, textId, Toast.LENGTH_SHORT).show()
}


