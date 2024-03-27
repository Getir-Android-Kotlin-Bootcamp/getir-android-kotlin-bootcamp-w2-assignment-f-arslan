package com.getir.patika.foodmap.ext

import android.view.ViewGroup
import com.getir.patika.foodmap.databinding.FragmentMapsBinding
import com.google.android.material.snackbar.Snackbar

fun FragmentMapsBinding.makeSnackbar(text: String) {
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
