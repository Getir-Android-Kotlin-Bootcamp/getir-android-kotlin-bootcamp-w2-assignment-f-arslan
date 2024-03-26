package com.getir.patika.foodmap.data.ext

import android.view.ViewGroup
import com.getir.patika.foodmap.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

fun ActivityMainBinding.makeSnackbar(text: String) {
    val snackbar = Snackbar.make(this.root, text, Snackbar.LENGTH_SHORT)
    val snackbarView = snackbar.view
    val params = snackbarView.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        params.leftMargin,
        params.topMargin,
        params.rightMargin,
        params.bottomMargin + 128
    )
    snackbarView.layoutParams = params
    snackbar.show()
}
