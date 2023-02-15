package com.example.rickandmorty.core.image

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {

    fun displayImage(url: String?, @DrawableRes placeholder: Int, imageView: ImageView, loading: (Boolean) -> Unit)
}