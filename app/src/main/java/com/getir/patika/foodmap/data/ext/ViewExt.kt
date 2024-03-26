package com.getir.patika.foodmap.data.ext

import android.graphics.Typeface
import android.widget.SearchView
import android.widget.TextView

fun SearchView.setCustomFont(path: String) {
    val id = context.resources.getIdentifier("android:id/search_src_text", null, null)
    (findViewById<TextView>(id))?.typeface = Typeface.createFromAsset(context.assets, path)
}
