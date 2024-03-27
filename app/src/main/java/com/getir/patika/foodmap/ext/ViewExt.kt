package com.getir.patika.foodmap.ext

import android.graphics.Typeface
import android.widget.SearchView
import android.widget.TextView

fun SearchView.setCustomFont() {
    val id = context.resources.getIdentifier("android:id/search_src_text", null, null)
    (findViewById<TextView>(id))?.typeface = Typeface.createFromAsset(context.assets, "fonts/poppins_medium.ttf")
}
