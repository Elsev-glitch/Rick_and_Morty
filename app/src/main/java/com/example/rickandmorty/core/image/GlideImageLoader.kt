package com.example.rickandmorty.core.image

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import javax.inject.Inject

class GlideImageLoader @Inject constructor(
    private val glide: RequestManager
): ImageLoader {

    override fun displayImage(
        url: String?,
        placeholder: Int,
        imageView: ImageView,
        loading: (Boolean) -> Unit
    ) {
        loading(true)
        glide
            .asBitmap()
            .load(url)
            .error(placeholder)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    loading(false)
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    loading(false)
                    return false
                }
            })
            .into(imageView)
    }
}